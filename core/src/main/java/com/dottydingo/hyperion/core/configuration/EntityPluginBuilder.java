package com.dottydingo.hyperion.core.configuration;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.core.endpoint.HttpMethod;
import com.dottydingo.hyperion.core.key.KeyConverter;
import com.dottydingo.hyperion.core.model.PersistentHistoryEntry;
import com.dottydingo.hyperion.core.model.PersistentObject;
import com.dottydingo.hyperion.core.persistence.*;
import com.dottydingo.hyperion.core.persistence.dao.Dao;
import com.dottydingo.hyperion.core.registry.ApiVersionPlugin;
import com.dottydingo.hyperion.core.registry.ApiVersionRegistry;
import com.dottydingo.hyperion.core.registry.EntityPlugin;
import org.springframework.beans.factory.FactoryBean;

import java.util.*;

/**
 */
public class EntityPluginBuilder
{
    protected String endpointName;
    protected Class<? extends PersistentObject> entityClass;
    protected KeyConverter keyConverter;

    protected HttpMethod[] limitMethods;
    protected int cacheMaxAge = 0;

    protected PersistenceOperations persistenceOperations;
    protected Dao dao;
    protected PersistenceFilter<PersistentObject> persistenceFilter;

    protected Boolean historyEnabled;
    protected Class<? extends PersistentHistoryEntry> historyType;

    protected CreateKeyProcessor defaultCreateKeyProcessor;

    protected List<EntityChangeListener> transactionalEntityChangeListeners;
    protected List<EntityChangeListener> entityChangeListeners;

    protected List<ApiVersionPluginBuilder> versions;


    public EntityPlugin build() throws Exception
    {
        validateRequired();

        EntityPlugin entityPlugin = new EntityPlugin();
        entityPlugin.setEndpointName(endpointName);
        entityPlugin.setEntityClass(entityClass);
        entityPlugin.setKeyConverter(getKeyConverter(keyConverter));
        if(entityPlugin.getKeyConverter() == null)
            throw new RuntimeException("keyConverter must be specified");

        if(limitMethods != null && limitMethods.length > 0)
            entityPlugin.setLimitMethods(new HashSet<HttpMethod>(Arrays.asList(limitMethods)));

        entityPlugin.setCacheMaxAge(cacheMaxAge);

        entityPlugin.setPersistenceOperations(persistenceOperations);
        entityPlugin.setDao(dao);
        entityPlugin.setPersistenceFilter(getPersistenceFilter(persistenceFilter));

        if(historyEnabled != null)
            entityPlugin.setHistoryEnabled(historyEnabled);

        entityPlugin.setHistoryType(historyType);
        if(transactionalEntityChangeListeners != null)
            entityPlugin.setTransactionalEntityChangeListeners(transactionalEntityChangeListeners);
        if(entityChangeListeners != null)
            entityPlugin.setEntityChangeListeners(entityChangeListeners);


        List<ApiVersionPlugin> apiVersionPlugins = new ArrayList<ApiVersionPlugin>();
        for (ApiVersionPluginBuilder versionBuilder : versions)
        {
            if(defaultCreateKeyProcessor != null)
                versionBuilder.setCreateKeyProcessor(defaultCreateKeyProcessor);

            apiVersionPlugins.add(versionBuilder.build());

        }
        ApiVersionRegistry apiVersionRegistry = new ApiVersionRegistry();
        apiVersionRegistry.setPlugins(apiVersionPlugins);
        entityPlugin.setApiVersionRegistry(apiVersionRegistry);

        return entityPlugin;
    }

    protected void validateRequired()
    {
        if(endpointName == null)
            throw new RuntimeException("endpointName must be specified");

        if(entityClass == null)
            throw new RuntimeException("entityClass must be specified");

        if(persistenceOperations == null)
            throw new RuntimeException("persistenceOperations must be specified");

        if(dao == null)
            throw new RuntimeException("dao must be specified");

        if(versions == null || versions.size() == 0)
            throw new RuntimeException("versions must be specified");

        if(historyEnabled != null && historyEnabled && historyType == null)
            throw new RuntimeException("historyType must be specified when history is enabled.");

    }

    protected KeyConverter getKeyConverter(KeyConverter keyConverter)
    {
        return keyConverter;
    }

    protected PersistenceFilter getPersistenceFilter(PersistenceFilter persistenceFilter)
    {
        if(persistenceFilter == null)
            return new EmptyPersistenceFilter();

        return persistenceFilter;
    }

    public String getEndpointName()
    {
        return endpointName;
    }

    public void setEndpointName(String endpointName)
    {
        this.endpointName = endpointName;
    }

    public Class<? extends PersistentObject> getEntityClass()
    {
        return entityClass;
    }

    public void setEntityClass(Class<? extends PersistentObject> entityClass)
    {
        this.entityClass = entityClass;
    }

    public KeyConverter getKeyConverter()
    {
        return keyConverter;
    }

    public void setKeyConverter(KeyConverter keyConverter)
    {
        this.keyConverter = keyConverter;
    }

    public HttpMethod[] getLimitMethods()
    {
        return limitMethods;
    }

    public void setLimitMethods(HttpMethod[] limitMethods)
    {
        this.limitMethods = limitMethods;
    }

    public int getCacheMaxAge()
    {
        return cacheMaxAge;
    }

    public void setCacheMaxAge(int cacheMaxAge)
    {
        this.cacheMaxAge = cacheMaxAge;
    }

    public PersistenceOperations getPersistenceOperations()
    {
        return persistenceOperations;
    }

    public void setPersistenceOperations(PersistenceOperations persistenceOperations)
    {
        this.persistenceOperations = persistenceOperations;
    }

    public Dao getDao()
    {
        return dao;
    }

    public void setDao(Dao dao)
    {
        this.dao = dao;
    }

    public PersistenceFilter<PersistentObject> getPersistenceFilter()
    {
        return persistenceFilter;
    }

    public void setPersistenceFilter(PersistenceFilter<PersistentObject> persistenceFilter)
    {
        this.persistenceFilter = persistenceFilter;
    }

    public Boolean getHistoryEnabled()
    {
        return historyEnabled;
    }

    public void setHistoryEnabled(Boolean historyEnabled)
    {
        this.historyEnabled = historyEnabled;
    }

    public Class<? extends PersistentHistoryEntry> getHistoryType()
    {
        return historyType;
    }

    public void setHistoryType(Class<? extends PersistentHistoryEntry> historyType)
    {
        this.historyType = historyType;
    }

    public CreateKeyProcessor getDefaultCreateKeyProcessor()
    {
        return defaultCreateKeyProcessor;
    }

    public void setDefaultCreateKeyProcessor(CreateKeyProcessor defaultCreateKeyProcessor)
    {
        this.defaultCreateKeyProcessor = defaultCreateKeyProcessor;
    }

    public List<EntityChangeListener> getTransactionalEntityChangeListeners()
    {
        return transactionalEntityChangeListeners;
    }

    public void setTransactionalEntityChangeListeners(List<EntityChangeListener> transactionalEntityChangeListeners)
    {
        this.transactionalEntityChangeListeners = transactionalEntityChangeListeners;
    }

    public List<EntityChangeListener> getEntityChangeListeners()
    {
        return entityChangeListeners;
    }

    public void setEntityChangeListeners(List<EntityChangeListener> entityChangeListeners)
    {
        this.entityChangeListeners = entityChangeListeners;
    }

    public List<ApiVersionPluginBuilder> getVersions()
    {
        return versions;
    }

    public void setVersions(List<ApiVersionPluginBuilder> versions)
    {
        this.versions = versions;
    }
}
