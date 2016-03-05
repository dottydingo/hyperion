package com.dottydingo.hyperion.client.builder;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.client.*;

import java.io.Serializable;

/**
 * A request builder for deletes
 */
public class DeleteRequestBuilder<T extends ApiObject<ID>,ID extends Serializable> extends RequestBuilder<T,ID>
{
    private ID[] ids;

    /**
     * Create the request builder using the specified parameters
     * @param version The entity version
     * @param objectType The API type
     * @param entityName The entity name
     * @param ids The ids to delete
     */
    public DeleteRequestBuilder(int version, Class<T> objectType, String entityName, ID[] ids)
    {
        super(version, objectType, entityName);
        this.ids = ids;
    }

    @Override
    public DeleteRequestBuilder<T, ID> addParameter(String name, String value)
    {
        super.addParameter(name, value);
        return this;
    }

    @Override
    public DeleteRequestBuilder<T, ID> setParameter(String name, String value)
    {
        super.setParameter(name, value);
        return this;
    }

    @Override
    public DeleteRequestBuilder<T, ID> addHeader(String name, String value)
    {
        super.addHeader(name, value);
        return this;
    }

    @Override
    public DeleteRequestBuilder<T, ID> setHeader(String name, String value)
    {
        super.setHeader(name, value);
        return this;
    }

    @Override
    public DeleteRequestBuilder<T, ID> withHeaderFactory(HeaderFactory headerFactory)
    {
        super.withHeaderFactory(headerFactory);
        return this;
    }

    @Override
    public DeleteRequestBuilder<T, ID> withParameterFactory(ParameterFactory parameterFactory)
    {
        super.withParameterFactory(parameterFactory);
        return this;
    }

    @Override
    public Request<T> build()
    {
        Request<T> request = super.build();
        request.setRequestMethod(RequestMethod.DELETE);
        request.setPath(join(ids));
        return request;
    }

    /**
     * Execute the request using the supplied client
     * @param client the client
     * @return The request
     */
    public int execute(HyperionClient client)
    {
        return client.delete(build());
    }
}
