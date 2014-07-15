package com.dottydingo.hyperion.client.builder;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.client.*;
import com.dottydingo.hyperion.client.exception.ClientException;

import java.io.Serializable;

/**
 */
public class UpdateRequestBuilder<T extends ApiObject<ID>,ID extends Serializable> extends RequestBuilder<T,ID>
{
    private T body;

    public UpdateRequestBuilder(int version, Class<T> objectType, String entityName, T body)
    {
        super(version, objectType, entityName);
        if(body.getId() == null)
            throw new ClientException("The ID field can not be null.");

        this.body = body;
    }

    public UpdateRequestBuilder<T, ID> returnFields(String... fields)
    {
        setParameter("fields",join(fields));
        return this;
    }

    @Override
    public UpdateRequestBuilder<T, ID> addParameter(String name, String value)
    {
        super.addParameter(name, value);
        return this;
    }

    @Override
    public UpdateRequestBuilder<T, ID> setParameter(String name, String value)
    {
        super.setParameter(name, value);
        return this;
    }

    @Override
    public UpdateRequestBuilder<T, ID> addHeader(String name, String value)
    {
        super.addHeader(name, value);
        return this;
    }

    @Override
    public UpdateRequestBuilder<T, ID> setHeader(String name, String value)
    {
        super.setHeader(name, value);
        return this;
    }

    @Override
    public UpdateRequestBuilder<T, ID> withHeaderFactory(HeaderFactory headerFactory)
    {
        super.withHeaderFactory(headerFactory);
        return this;
    }

    @Override
    public UpdateRequestBuilder<T, ID> withParameterFactory(ParameterFactory parameterFactory)
    {
        super.withParameterFactory(parameterFactory);
        return this;
    }

    @Override
    public Request<T> build()
    {
        Request<T> request = super.build();
        request.setRequestBody(body);
        request.setRequestMethod(RequestMethod.PUT);
        request.setPath(body.getId().toString());
        return request;
    }

    public T execute(HyperionClient client)
    {
        return client.update(build());
    }
}
