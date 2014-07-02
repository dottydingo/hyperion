package com.dottydingo.hyperion.core.configuration;

import com.dottydingo.hyperion.core.key.KeyConverter;
import com.dottydingo.hyperion.core.model.PersistentHistoryEntry;
import com.dottydingo.hyperion.core.persistence.PersistenceOperations;
import com.dottydingo.hyperion.core.persistence.dao.Dao;
import com.dottydingo.hyperion.core.registry.EntityPlugin;
import com.dottydingo.hyperion.core.registry.ServiceRegistry;
import org.springframework.beans.factory.FactoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class ServiceRegistryBuilder  implements FactoryBean<ServiceRegistry>
{
    private KeyConverter defaultKeyConverter;
    private PersistenceOperations defaultPersistenceOperations;
    private Dao defaultDao;
    private Boolean defaultHistoryEnabled;
    private Class<? extends PersistentHistoryEntry> defaultHistoryType;

    private List<EntityPluginBuilder> entities;

    @Override
    public ServiceRegistry getObject() throws Exception
    {
        validateRequired();

        List<EntityPlugin> plugins = new ArrayList<EntityPlugin>();

        for (EntityPluginBuilder pluginBuilder : entities)
        {
            if(pluginBuilder.getKeyConverter() == null)
                pluginBuilder.setKeyConverter(defaultKeyConverter);

            if(pluginBuilder.getPersistenceOperations() == null)
                pluginBuilder.setPersistenceOperations(defaultPersistenceOperations);

            if(pluginBuilder.getDao() == null)
                pluginBuilder.setDao(defaultDao);

            if(pluginBuilder.getHistoryEnabled() == null)
                pluginBuilder.setHistoryEnabled(defaultHistoryEnabled);

            if(pluginBuilder.getHistoryType() == null)
                pluginBuilder.setHistoryType(defaultHistoryType);

            plugins.add(pluginBuilder.build());
        }

        ServiceRegistry serviceRegistry = new ServiceRegistry();
        serviceRegistry.setEntityPlugins(plugins);

        return serviceRegistry;
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

    protected void validateRequired()
    {
        if(entities == null)
            throw new RuntimeException("entities must be specified.");
    }

    public void setDefaultKeyConverter(KeyConverter defaultKeyConverter)
    {
        this.defaultKeyConverter = defaultKeyConverter;
    }

    public void setDefaultPersistenceOperations(PersistenceOperations defaultPersistenceOperations)
    {
        this.defaultPersistenceOperations = defaultPersistenceOperations;
    }

    public void setDefaultDao(Dao defaultDao)
    {
        this.defaultDao = defaultDao;
    }

    public void setDefaultHistoryEnabled(Boolean defaultHistoryEnabled)
    {
        this.defaultHistoryEnabled = defaultHistoryEnabled;
    }

    public void setDefaultHistoryType(Class<? extends PersistentHistoryEntry> defaultHistoryType)
    {
        this.defaultHistoryType = defaultHistoryType;
    }

    public void setEntities(List<EntityPluginBuilder> entities)
    {
        this.entities = entities;
    }
}
