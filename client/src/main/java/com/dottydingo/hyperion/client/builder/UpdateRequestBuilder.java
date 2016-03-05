package com.dottydingo.hyperion.client.builder;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.EntityList;
import com.dottydingo.hyperion.client.Request;
import com.dottydingo.hyperion.client.RequestMethod;

import java.io.Serializable;
import java.util.List;

/**
 * A base request builder for updates
 */
public abstract class UpdateRequestBuilder<T extends ApiObject<ID>, ID extends Serializable> extends RequestBuilder<T,ID>
{
    protected List<T> entries;

    /**
     * Create the request builder using the specified parameters
     * @param version The entity version
     * @param objectType The API type
     * @param entityName The entity name
     * @param entries The entries to update
     */
    protected UpdateRequestBuilder(int version, Class<T> objectType, String entityName, List<T> entries)
    {
        super(version, objectType, entityName);
        this.entries = entries;
    }

    /**
     * Set the fields to return in the request. The default is all fields.
     * @param fields The fields to return
     * @return The request builder
     */
    public UpdateRequestBuilder<T, ID> returnFields(String... fields)
    {
        setParameter("fields",join(fields));
        return this;
    }

    @Override
    public Request<T> build()
    {
        Request<T> request = super.build();
        EntityList<T> entityResponse = new EntityList<>();
        entityResponse.setEntries(entries);
        request.setRequestBody(entityResponse);
        request.setRequestMethod(RequestMethod.PUT);
        return request;
    }
}
