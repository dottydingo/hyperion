package com.dottydingo.hyperion.client.builder;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.client.*;

import java.io.Serializable;

/**
 */
public class CreateRequestBuilder<T extends ApiObject<ID>,ID extends Serializable> extends RequestBuilder<T,ID>
{
    private T body;

    public CreateRequestBuilder(int version, Class<T> objectType,String entityName, T body)
    {
        super(version, objectType, entityName);
        this.body = body;
    }

    public CreateRequestBuilder<T, ID> returnFields(String... fields)
    {
        setParameter("fields",join(fields));
        return this;
    }

    @Override
    public CreateRequestBuilder<T, ID> addParameter(String name, String value)
    {
        super.addParameter(name, value);
        return this;
    }

    @Override
    public CreateRequestBuilder<T, ID> setParameter(String name, String value)
    {
        super.setParameter(name, value);
        return this;
    }

    @Override
    public CreateRequestBuilder<T, ID> addHeader(String name, String value)
    {
        super.addHeader(name, value);
        return this;
    }

    @Override
    public CreateRequestBuilder<T, ID> setHeader(String name, String value)
    {
        super.setHeader(name, value);
        return this;
    }

    @Override
    public CreateRequestBuilder<T, ID> withHeaderFactory(HeaderFactory headerFactory)
    {
        super.withHeaderFactory(headerFactory);
        return this;
    }

    @Override
    public CreateRequestBuilder<T, ID> withParameterFactory(ParameterFactory parameterFactory)
    {
        super.withParameterFactory(parameterFactory);
        return this;
    }

    @Override
    public Request<T> build()
    {
        Request<T> request = super.build();
        request.setRequestBody(body);
        request.setRequestMethod(RequestMethod.POST);
        return request;
    }

    public T execute(HyperionClient client)
    {
        return client.create(build());
    }
}
