package com.yammer.dropwizard.cli;

import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.databind.Module;
import com.google.common.collect.ImmutableList;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.ConfigurationException;
import com.yammer.dropwizard.config.ConfigurationFactory;
import com.yammer.dropwizard.config.LoggingFactory;
import com.yammer.dropwizard.validation.Validator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A command whose first parameter is the location of a YAML configuration file. That file is parsed
 * into an instance of a {@link Configuration} subclass, which is then validated. If the
 * configuration is valid, the command is run.
 *
 * @param <T> the {@link Configuration} subclass which is loaded from the configuration file
 * @see Configuration
 */
public abstract class ConfiguredCommand<T extends Configuration> implements Command {
    @Parameter(description = "<configuration file>")
    @SuppressWarnings("FieldMayBeFinal")
    private List<String> arguments = new ArrayList<String>(1);

    @SuppressWarnings("unchecked")
    protected Class<T> getConfigurationClass() {
        Type t = getClass();
        while (t instanceof Class<?>) {
            t = ((Class<?>) t).getGenericSuperclass();
        }
        /* This is not guaranteed to work for all cases with convoluted piping
         * of type parameters: but it can at least resolve straight-forward
         * extension with single type parameter (as per [Issue-89]).
         * And when it fails to do that, will indicate with specific exception.
         */
        if (t instanceof ParameterizedType) {
            // should typically have one of type parameters (first one) that matches:
            for (Type param : ((ParameterizedType) t).getActualTypeArguments()) {
                if (param instanceof Class<?>) {
                    final Class<?> cls = (Class<?>) param;
                    if (Configuration.class.isAssignableFrom(cls)) {
                        return (Class<T>) cls;
                    }
                }
            }
        }
        throw new IllegalStateException("Cannot figure out Configuration type parameterization for " +
                                                getClass().getName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run(Service<?> service) throws Exception {
        final T configuration = parseConfiguration(getConfigurationClass(),
                                                   service.getJacksonModules());

        if (configuration != null) {
            new LoggingFactory(configuration.getLoggingConfiguration(), service.getName()).configure();
            run((Service<T>)service, configuration);
        }
    }

    private T parseConfiguration(Class<T> configurationClass,
                                 ImmutableList<Module> modules) throws IOException, ConfigurationException {
        final ConfigurationFactory<T> configurationFactory =
                ConfigurationFactory.forClass(configurationClass, new Validator(), modules);
        if (arguments.size() >= 1) {
            final String file = arguments.remove(0);
            return configurationFactory.build(new File(file));
        }

        return configurationFactory.build();
    }

    /**
     * Runs the command with the given {@link com.yammer.dropwizard.Service} and {@link Configuration}.
     *
     * @param service          the service to which the command belongs
     * @param configuration    the configuration object
     * @throws Exception if something goes wrong
     */
    protected abstract void run(Service<T> service,
                                T configuration) throws  Exception;

    protected final List<String> getArguments() {
        return arguments;
    }
}
