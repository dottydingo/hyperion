package com.dottydingo.hyperion.core.configuration;

import com.dottydingo.hyperion.core.registry.ServiceRegistry;
import org.springframework.beans.factory.FactoryBean;

/**
 * A Spring FactoryBean for building a Service Registry
 */
public class SpringServiceRegistryBuilder extends ServiceRegistryBuilder implements FactoryBean<ServiceRegistry>
{
    @Override
    public ServiceRegistry getObject() throws Exception
    {
        return build();
    }

    @Override
    public Class<?> getObjectType()
    {
        return ServiceRegistry.class;
    }

    @Override
    public boolean isSingleton()
    {
        return true;
    }

}
