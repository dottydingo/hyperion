package com.dottydingo.hyperion.service.configuration;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.service.context.HttpMethod;
import com.dottydingo.hyperion.service.model.BasePersistentHistoryEntry;
import com.dottydingo.hyperion.service.model.PersistentObject;
import com.dottydingo.hyperion.service.persistence.*;
import com.dottydingo.hyperion.service.key.KeyConverter;
import com.dottydingo.hyperion.service.persistence.dao.Dao;
import com.dottydingo.hyperion.service.persistence.query.QueryBuilder;
import com.dottydingo.hyperion.service.persistence.sort.SortBuilder;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 */
public class EntityPlugin<C extends ApiObject,P extends PersistentObject,ID extends Serializable>
{
    private String endpointName;
    private KeyConverter<ID> keyConverter;
    private PersistenceOperations<C,ID> persistenceOperations;
    private ApiVersionRegistry<C,P> apiVersionRegistry;
    private Set<HttpMethod> limitMethods = new HashSet<HttpMethod>();
    private Map<String,SortBuilder> sortBuilders;
    private Class<P> entityClass;
    private Map<String,QueryBuilder> queryBuilders;
    private CreateKeyProcessor<C,ID> createKeyProcessor;
    private boolean historyEnabled = false;
    private Class<? extends BasePersistentHistoryEntry> historyType;
    private PersistenceFilter<P> persistenceFilter = new EmptyPersistenceFilter<P>();
    private int cacheMaxAge = 0;
    private Dao<P,ID> dao;
    private EntityChangeListener<C> entityChangeListener;

    public String getEndpointName()
    {
        return endpointName;
    }

    public void setEndpointName(String endpointName)
    {
        this.endpointName = endpointName;
    }

    public KeyConverter<ID> getKeyConverter()
    {
        return keyConverter;
    }

    public void setKeyConverter(KeyConverter<ID> keyConverter)
    {
        this.keyConverter = keyConverter;
    }

    public PersistenceOperations<C,ID> getPersistenceOperations()
    {
        return persistenceOperations;
    }

    public void setPersistenceOperations(PersistenceOperations<C,ID> persistenceOperations)
    {
        this.persistenceOperations = persistenceOperations;
    }

    public ApiVersionRegistry<C,P> getApiVersionRegistry()
    {
        return apiVersionRegistry;
    }

    public void setApiVersionRegistry(ApiVersionRegistry<C,P> apiVersionRegistry)
    {
        this.apiVersionRegistry = apiVersionRegistry;
    }


    public void setLimitMethods(Set<HttpMethod> limitMethods)
    {
        this.limitMethods = limitMethods;
        if(!limitMethods.isEmpty()) // always allow OPTIONS
            limitMethods.add(HttpMethod.OPTIONS);
    }

    public boolean isMethodAllowed(HttpMethod method)
    {
        return limitMethods.isEmpty() || limitMethods.contains(method);
    }

    public void filterAllowedMethods(Set<HttpMethod> methods)
    {
        if(!limitMethods.isEmpty())
            methods.retainAll(limitMethods);
    }

    public Map<String, SortBuilder> getSortBuilders()
    {
        return sortBuilders;
    }

    public void setSortBuilders(Map<String, SortBuilder> sortBuilders)
    {
        this.sortBuilders = sortBuilders;
    }

    public Class<P> getEntityClass()
    {
        return entityClass;
    }

    public void setEntityClass(Class<P> entityClass)
    {
        this.entityClass = entityClass;
    }

    public Map<String, QueryBuilder> getQueryBuilders()
    {
        return queryBuilders;
    }

    public void setQueryBuilders(Map<String, QueryBuilder> queryBuilders)
    {
        this.queryBuilders = queryBuilders;
    }

    public CreateKeyProcessor<C, ID> getCreateKeyProcessor()
    {
        return createKeyProcessor;
    }

    public void setCreateKeyProcessor(CreateKeyProcessor<C, ID> createKeyProcessor)
    {
        this.createKeyProcessor = createKeyProcessor;
    }

    public boolean isHistoryEnabled()
    {
        return historyEnabled;
    }

    public void setHistoryEnabled(boolean historyEnabled)
    {
        this.historyEnabled = historyEnabled;
    }

    public Class<? extends BasePersistentHistoryEntry> getHistoryType()
    {
        return historyType;
    }

    public void setHistoryType(Class<? extends BasePersistentHistoryEntry> historyType)
    {
        this.historyType = historyType;
    }

    public PersistenceFilter<P> getPersistenceFilter()
    {
        return persistenceFilter;
    }

    public void setPersistenceFilter(PersistenceFilter<P> persistenceFilter)
    {
        this.persistenceFilter = persistenceFilter;
    }

    public int getCacheMaxAge()
    {
        return cacheMaxAge;
    }

    public void setCacheMaxAge(int cacheMaxAge)
    {
        this.cacheMaxAge = cacheMaxAge;
    }

    public Dao<P, ID> getDao()
    {
        return dao;
    }

    public void setDao(Dao<P, ID> dao)
    {
        this.dao = dao;
    }

    public Set<HttpMethod> getLimitMethods()
    {
        return limitMethods;
    }

    public EntityChangeListener<C> getEntityChangeListener()
    {
        return entityChangeListener;
    }

    public void setEntityChangeListener(EntityChangeListener<C> entityChangeListener)
    {
        this.entityChangeListener = entityChangeListener;
    }
}
