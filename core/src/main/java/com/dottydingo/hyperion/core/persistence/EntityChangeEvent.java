package com.dottydingo.hyperion.core.persistence;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.core.endpoint.HttpMethod;

import java.util.Set;

/**
 */
public class EntityChangeEvent<C extends ApiObject>
{
    private String endpointName;
    private C originalItem;
    private C updatedItem;
    private Set<String> updatedFields;
    private PersistenceContext persistenceContext;

    public EntityChangeEvent(String endpointName, C originalItem, C updatedItem, Set<String> updatedFields,
                             PersistenceContext persistenceContext)
    {
        this.endpointName = endpointName;
        this.originalItem = originalItem;
        this.updatedItem = updatedItem;
        this.updatedFields = updatedFields;
        this.persistenceContext = persistenceContext;
    }

    public String getEndpointName()
    {
        return endpointName;
    }

    public C getOriginalItem()
    {
        return originalItem;
    }

    public C getUpdatedItem()
    {
        return updatedItem;
    }

    public Set<String> getUpdatedFields()
    {
        return updatedFields;
    }

    @Deprecated
    public HttpMethod getHttpMethod()
    {
        return persistenceContext.getHttpMethod();
    }

    public PersistenceContext getPersistenceContext()
    {
        return persistenceContext;
    }
}
