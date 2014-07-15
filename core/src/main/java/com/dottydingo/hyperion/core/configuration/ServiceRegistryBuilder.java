package com.dottydingo.hyperion.core.configuration;

import com.dottydingo.hyperion.core.key.KeyConverter;
import com.dottydingo.hyperion.core.model.PersistentHistoryEntry;
import com.dottydingo.hyperion.core.persistence.event.EntityChangeListener;
import com.dottydingo.hyperion.core.persistence.PersistenceOperations;
import com.dottydingo.hyperion.core.persistence.dao.Dao;
import com.dottydingo.hyperion.core.persistence.event.PersistentChangeListener;
import com.dottydingo.hyperion.core.registry.EntityPlugin;
import com.dottydingo.hyperion.core.registry.ServiceRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Convenience class for building a service registry
 */
public class ServiceRegistryBuilder
{
    private KeyConverter defaultKeyConverter;
    private PersistenceOperations defaultPersistenceOperations;
    private Dao defaultDao;
    private Boolean defaultHistoryEnabled;
    private Class<? extends PersistentHistoryEntry> defaultHistoryType;
    private List<PersistentChangeListener> persistentChangeListeners = Collections.emptyList();
    private List<EntityChangeListener> entityChangeListeners = Collections.emptyList();

    private List<EntityPluginBuilder> entities;

    public ServiceRegistry build() throws Exception
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


            plugins.add(pluginBuilder.build(this));
        }

        ServiceRegistry serviceRegistry = new ServiceRegistry();
        serviceRegistry.setEntityPlugins(plugins);

        return serviceRegistry;
    }

    protected void validateRequired()
    {
        if(entities == null || entities.size() == 0)
            throw new RuntimeException("entities must be specified.");
    }

    /**
     * Set the default key converter to use. This will be applied to any EntityPlugin instance
     * that doesn't specify a key converter
     * @param defaultKeyConverter the key converter
     */
    public void setDefaultKeyConverter(KeyConverter defaultKeyConverter)
    {
        this.defaultKeyConverter = defaultKeyConverter;
    }

    protected KeyConverter getDefaultKeyConverter()
    {
        return defaultKeyConverter;
    }

    /**
     * Set the default persistence operations instance to use. This will be applied to any EntityPlugin instance
     * that doesn't specify a persistence operations instance
     * @param defaultPersistenceOperations the persistence operations instance
     */
    public void setDefaultPersistenceOperations(PersistenceOperations defaultPersistenceOperations)
    {
        this.defaultPersistenceOperations = defaultPersistenceOperations;
    }

    protected PersistenceOperations getDefaultPersistenceOperations()
    {
        return defaultPersistenceOperations;
    }

    /**
     * Set the default Dao to use. This will be applied to any EntityPlugin instance that does not specify
     * a Dao instance.
     * @param defaultDao the dao
     */
    public void setDefaultDao(Dao defaultDao)
    {
        this.defaultDao = defaultDao;
    }

    protected Dao getDefaultDao()
    {
        return defaultDao;
    }

    /**
     * Set the transactional entity change listeners to apply to all entities
     * @param persistentChangeListeners The change listeners
     */
    public void setPersistentChangeListeners(List<PersistentChangeListener> persistentChangeListeners)
    {
        this.persistentChangeListeners = persistentChangeListeners;
    }

    protected List<PersistentChangeListener> getPersistentChangeListeners()
    {
        return persistentChangeListeners;
    }

    /**
     * Set the post transaction entity change listeners to apply to all entities
     * @param entityChangeListeners the change listeners
     */
    public void setEntityChangeListeners(List<EntityChangeListener> entityChangeListeners)
    {
        this.entityChangeListeners = entityChangeListeners;
    }

    protected List<EntityChangeListener> getEntityChangeListeners()
    {
        return entityChangeListeners;
    }

    protected Boolean getDefaultHistoryEnabled()
    {
        return defaultHistoryEnabled;
    }

    /**
     * Set if history should be enabled by default. If set this value will be applied any EntityPlugin instance that
     * does not specify it explicitly.
     * @param defaultHistoryEnabled the flag
     */
    public void setDefaultHistoryEnabled(Boolean defaultHistoryEnabled)
    {
        this.defaultHistoryEnabled = defaultHistoryEnabled;
    }

    protected Class<? extends PersistentHistoryEntry> getDefaultHistoryType()
    {
        return defaultHistoryType;
    }

    /**
     * Set the default history type. If set this value will be applied any EntityPlugin instance that
     * does not specify it explicitly.
     * @param defaultHistoryType the history entry type
     */
    public void setDefaultHistoryType(Class<? extends PersistentHistoryEntry> defaultHistoryType)
    {
        this.defaultHistoryType = defaultHistoryType;
    }

    /**
     * Set the entity plugin builders for the service
     * @param entities the entity plugin builders
     */
    public void setEntities(List<EntityPluginBuilder> entities)
    {
        this.entities = entities;
    }
}
