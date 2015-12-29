package com.dottydingo.hyperion.spring.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;

/**
 * Add default properties to environment
 */
public class HyperionInitializer implements SpringApplicationRunListener
{
    private Logger logger = LoggerFactory.getLogger(getClass());

    public HyperionInitializer(SpringApplication application, String[] args)
    {
    }

    @Override
    public void started()
    {

    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment)
    {
        logger.info("Adding Hyperion environment properties");
        try
        {
            ResourcePropertySource propertySource = new ResourcePropertySource("Hyperion","classpath:/com/dottydingo/hyperion/core/spring/hyperion_default.properties");
            environment.getPropertySources().addLast(propertySource);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Error adding hyperion_default.properties to environment.");
        }
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context)
    {

    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context)
    {

    }

    @Override
    public void finished(ConfigurableApplicationContext context, Throwable exception)
    {

    }
}
