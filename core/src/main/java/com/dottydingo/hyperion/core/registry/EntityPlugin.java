package com.dottydingo.hyperion.core.registry;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.exception.InternalException;
import com.dottydingo.hyperion.core.endpoint.HttpMethod;
import com.dottydingo.hyperion.core.model.PersistentHistoryEntry;
import com.dottydingo.hyperion.core.model.PersistentObject;
import com.dottydingo.hyperion.core.key.KeyConverter;
import com.dottydingo.hyperion.core.persistence.*;
import com.dottydingo.hyperion.core.persistence.dao.Dao;
import com.dottydingo.hyperion.core.persistence.event.EntityChangeListener;
import com.dottydingo.hyperion.core.persistence.event.PersistentChangeListener;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 */
public class EntityPlugin<C extends ApiObject,P extends PersistentObject,ID extends Serializable>
{
    private static final Pattern RESERVED_PARAMETERS = Pattern.compile("start|limit|query|fields|sort|version|trace|cid",
            Pattern.CASE_INSENSITIVE);

    private String endpointName;
    private Class<P> entityClass;
    private KeyConverter<ID> keyConverter;

    private Set<HttpMethod> limitMethods = new HashSet<HttpMethod>();
    private int cacheMaxAge = 0;

    private PersistenceOperations<C,ID> persistenceOperations;
    private Dao<P,ID,?,?> dao;
    private PersistenceFilter<P> persistenceFilter = new EmptyPersistenceFilter<P>();

    private boolean historyEnabled = false;
    private Class<? extends PersistentHistoryEntry> historyType;

    private ApiVersionRegistry<C,P> apiVersionRegistry;

    private List<PersistentChangeListener<C,ID>> persistentChangeListeners = Collections.emptyList();
    private List<EntityChangeListener<C>> entityChangeListeners = Collections.emptyList();

    private Set<String> additionalParameters = new HashSet<>();

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

    public Class<P> getEntityClass()
    {
        return entityClass;
    }

    public void setEntityClass(Class<P> entityClass)
    {
        this.entityClass = entityClass;
    }

    public boolean isHistoryEnabled()
    {
        return historyEnabled;
    }

    public void setHistoryEnabled(boolean historyEnabled)
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

    public Dao<P, ID,?,?> getDao()
    {
        return dao;
    }

    public void setDao(Dao<P, ID,?,?> dao)
    {
        this.dao = dao;
    }

    public Set<HttpMethod> getLimitMethods()
    {
        return limitMethods;
    }

    public List<PersistentChangeListener<C,ID>> getPersistentChangeListeners()
    {
        return persistentChangeListeners;
    }

    public void setPersistentChangeListeners(List<PersistentChangeListener<C, ID>> persistentChangeListeners)
    {
        this.persistentChangeListeners = persistentChangeListeners;
    }

    public List<EntityChangeListener<C>> getEntityChangeListeners()
    {
        return entityChangeListeners;
    }

    public void setEntityChangeListeners(List<EntityChangeListener<C>> entityChangeListeners)
    {
        this.entityChangeListeners = entityChangeListeners;
    }

    public boolean hasEntityChangeListeners()
    {
        return !entityChangeListeners.isEmpty();
    }

    public boolean hasPersistentChangeListeners()
    {
        return !persistentChangeListeners.isEmpty();
    }

    public boolean hasListeners()
    {
        return hasEntityChangeListeners() || hasPersistentChangeListeners();
    }

    public Set<String> getAdditionalParameters()
    {
        return additionalParameters;
    }

    public void setAdditionalParameters(Set<String> additionalParameters)
    {
        if(additionalParameters != null)
        {
            for (String additionalParameter : additionalParameters)
            {
                if(isReserved(additionalParameter))
                    throw new InternalException(String.format("The %s parameter is reserved.",additionalParameter));
            }
        }
        this.additionalParameters = additionalParameters;
    }

    protected boolean isReserved(String additionalParameter)
    {
        return RESERVED_PARAMETERS.matcher(additionalParameter).matches();
    }
}
