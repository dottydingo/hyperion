package com.dottydingo.hyperion.client.builder;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.EntityList;
import com.dottydingo.hyperion.client.*;

import java.io.Serializable;
import java.util.List;

/**
 */
public abstract class CreateRequestBuilder<T extends ApiObject<ID>,ID extends Serializable> extends RequestBuilder<T,ID>
{
    private List<T> entries;

    protected CreateRequestBuilder(int version, Class<T> objectType,String entityName, List<T> entries)
    {
        super(version, objectType, entityName);
        this.entries = entries;
    }

    public CreateRequestBuilder<T, ID> returnFields(String... fields)
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
        request.setRequestMethod(RequestMethod.POST);
        return request;
    }


}
