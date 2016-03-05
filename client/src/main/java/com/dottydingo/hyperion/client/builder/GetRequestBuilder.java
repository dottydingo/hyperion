package com.dottydingo.hyperion.client.builder;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.EntityList;
import com.dottydingo.hyperion.api.EntityResponse;
import com.dottydingo.hyperion.client.*;

import java.io.Serializable;

/**
 * A request builder for finds
 */
public class GetRequestBuilder<T extends ApiObject<ID>,ID extends Serializable> extends RequestBuilder<T,ID>
{
    private ID[] ids;

    /**
     * Create the request builder using the specified parameters
     * @param version The entity version
     * @param objectType The API type
     * @param entityName The entity name
     * @param ids The ids to add
     */
    public GetRequestBuilder(int version, Class<T> objectType, String entityName, ID[] ids)
    {
        super(version, objectType, entityName);
        this.ids = ids;
    }

    /**
     * Set the fields to return in the request. The default is all fields.
     * @param fields The fields to return
     * @return The request builder
     */
    public GetRequestBuilder<T, ID> returnFields(String... fields)
    {
        setParameter("fields",join(fields));
        return this;
    }

    @Override
    public GetRequestBuilder<T, ID> addParameter(String name, String value)
    {
        super.addParameter(name, value);
        return this;
    }

    @Override
    public GetRequestBuilder<T, ID> setParameter(String name, String value)
    {
        super.setParameter(name, value);
        return this;
    }

    @Override
    public GetRequestBuilder<T, ID> addHeader(String name, String value)
    {
        super.addHeader(name, value);
        return this;
    }

    @Override
    public GetRequestBuilder<T, ID> setHeader(String name, String value)
    {
        super.setHeader(name, value);
        return this;
    }

    @Override
    public GetRequestBuilder<T, ID> withHeaderFactory(HeaderFactory headerFactory)
    {
        super.withHeaderFactory(headerFactory);
        return this;
    }

    @Override
    public GetRequestBuilder<T, ID> withParameterFactory(ParameterFactory parameterFactory)
    {
        super.withParameterFactory(parameterFactory);
        return this;
    }

    @Override
    public Request<T> build()
    {
        Request<T> request = super.build();
        request.setPath(join(ids));
        request.setRequestMethod(RequestMethod.GET);
        return request;
    }

    /**
     * Execute the request using the supplied client
     * @param client the client
     * @return The request
     */
    public EntityList<T> execute(HyperionClient client)
    {
        return client.get(build());
    }
}
