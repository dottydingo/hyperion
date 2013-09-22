package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.service.endpoint.HttpMethod;

import java.util.Set;

/**
 */
public class EntityChangeEvent<C extends ApiObject>
{
    private String endpointName;
    private C originalItem;
    private C updatedItem;
    private Set<String> updatedFields;
    private HttpMethod httpMethod;

    public EntityChangeEvent(String endpointName, C originalItem, C updatedItem, Set<String> updatedFields, HttpMethod httpMethod)
    {
        this.endpointName = endpointName;
        this.originalItem = originalItem;
        this.updatedItem = updatedItem;
        this.updatedFields = updatedFields;
        this.httpMethod = httpMethod;
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

    public HttpMethod getHttpMethod()
    {
        return httpMethod;
    }
}
