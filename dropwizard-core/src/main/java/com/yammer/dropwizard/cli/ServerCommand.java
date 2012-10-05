package com.yammer.dropwizard.cli;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.config.ServerFactory;
import com.yammer.dropwizard.logging.Log;
import net.sourceforge.argparse4j.inf.Namespace;
import org.eclipse.jetty.server.Server;

import java.io.IOException;

// TODO: 10/12/11 <coda> -- write tests for ServerCommand

/**
 * Runs a service as an HTTP server.
 *
 * @param <T> the {@link Configuration} subclass which is loaded from the configuration file
 */
public class ServerCommand<T extends Configuration> extends EnvironmentCommand<T> {
    private final Class<T> configurationClass;

    public ServerCommand(Service<T> service) {
        super(service, "server", "Runs the Dropwizard service as an HTTP server");
        this.configurationClass = service.getConfigurationClass();
    }

    /*
     * Since we don't subclass ServerCommand, we need a concrete reference to the configuration
     * class.
     */
    @Override
    protected Class<T> getConfigurationClass() {
        return configurationClass;
    }

    @Override
    protected void run(Environment environment, Namespace namespace, T configuration) throws Exception {
        final Server server = new ServerFactory(configuration.getHttpConfiguration(),
                                                environment.getName()).buildServer(environment);
        final Log log = Log.forClass(ServerCommand.class);
        logBanner(environment.getName(), log);
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            log.error(e, "Unable to start server, shutting down");
            server.stop();
        }
    }

    private void logBanner(String name, Log log) {
        try {
            final String banner = Resources.toString(Resources.getResource("banner.txt"),
                                                     Charsets.UTF_8);
            log.info("Starting {}\n{}", name, banner);
        } catch (IllegalArgumentException ignored) {
            // don't display the banner if there isn't one
            log.info("Starting {}", name);
        } catch (IOException ignored) {
            log.info("Starting {}", name);
        }
    }
}
