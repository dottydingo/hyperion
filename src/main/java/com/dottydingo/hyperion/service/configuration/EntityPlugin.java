package com.dottydingo.hyperion.service.configuration;

import com.dottydingo.hyperion.api.BaseApiObject;
import com.dottydingo.hyperion.service.persistence.PersistenceOperations;
import com.dottydingo.hyperion.service.key.KeyConverter;
import com.dottydingo.hyperion.service.model.BasePersistentObject;

import java.io.Serializable;

/**
 */
public class EntityPlugin<C extends BaseApiObject,P extends BasePersistentObject,ID extends Serializable>
{
    private String endpointName;
    private KeyConverter<ID> keyConverter;
    private PersistenceOperations<P,ID> persistenceOperations;
    private ApiVersionRegistry<C,P> apiVersionRegistry;

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

    public PersistenceOperations<P, ID> getPersistenceOperations()
    {
        return persistenceOperations;
    }

    public void setPersistenceOperations(PersistenceOperations<P, ID> persistenceOperations)
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
}
