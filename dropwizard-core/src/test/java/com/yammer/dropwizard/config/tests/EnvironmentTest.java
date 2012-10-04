package com.yammer.dropwizard.config.tests;

import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.json.ObjectMapperFactory;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class EnvironmentTest {

    @Test
    @SuppressWarnings("unchecked")
    public void scanPackagesHandlesEmptyArgumentList() {
        new Environment("", mock(Configuration.class), mock(ObjectMapperFactory.class)).scanPackagesForResourcesAndProviders();
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("unchecked")
    public void scanPackagesThrowsNpeOnNullArgument() {
        new Environment("", mock(Configuration.class), mock(ObjectMapperFactory.class)).scanPackagesForResourcesAndProviders(
                (Class<?>[]) null);
    }
}
