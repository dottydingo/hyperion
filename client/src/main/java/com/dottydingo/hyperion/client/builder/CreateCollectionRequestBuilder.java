package com.dottydingo.hyperion.client.builder;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.EntityResponse;
import com.dottydingo.hyperion.client.*;

import java.io.Serializable;
import java.util.List;

/**
 */
public class CreateCollectionRequestBuilder<T extends ApiObject<ID>,ID extends Serializable> extends RequestBuilder<T,ID>
{
    private List<T> entries;

    public CreateCollectionRequestBuilder(int version, Class<T> objectType, String entityName, List<T> entries)
    {
        super(version, objectType, entityName);
        this.entries = entries;
        setParameter("collection","true");
    }

    public CreateCollectionRequestBuilder<T, ID> returnFields(String... fields)
    {
        setParameter("fields",join(fields));
        return this;
    }

    @Override
    public CreateCollectionRequestBuilder<T, ID> addParameter(String name, String value)
    {
        super.addParameter(name, value);
        return this;
    }

    @Override
    public CreateCollectionRequestBuilder<T, ID> setParameter(String name, String value)
    {
        super.setParameter(name, value);
        return this;
    }

    @Override
    public CreateCollectionRequestBuilder<T, ID> addHeader(String name, String value)
    {
        super.addHeader(name, value);
        return this;
    }

    @Override
    public CreateCollectionRequestBuilder<T, ID> setHeader(String name, String value)
    {
        super.setHeader(name, value);
        return this;
    }

    @Override
    public CreateCollectionRequestBuilder<T, ID> withHeaderFactory(HeaderFactory headerFactory)
    {
        super.withHeaderFactory(headerFactory);
        return this;
    }

    @Override
    public CreateCollectionRequestBuilder<T, ID> withParameterFactory(ParameterFactory parameterFactory)
    {
        super.withParameterFactory(parameterFactory);
        return this;
    }

    @Override
    public Request<T> build()
    {
        Request<T> request = super.build();
        EntityResponse<T> entityResponse = new EntityResponse<>();
        entityResponse.setEntries(entries);
        request.setRequestBody(entityResponse);
        request.setRequestMethod(RequestMethod.POST);
        return request;
    }

    public List<T> execute(HyperionClient client)
    {
        EntityResponse<T> response = client.createCollection(build());
        return response.getEntries();
    }
}
