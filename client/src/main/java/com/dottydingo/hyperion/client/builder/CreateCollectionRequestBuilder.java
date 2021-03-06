package com.dottydingo.hyperion.client.builder;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.EntityList;
import com.dottydingo.hyperion.client.HeaderFactory;
import com.dottydingo.hyperion.client.HyperionClient;
import com.dottydingo.hyperion.client.ParameterFactory;

import java.io.Serializable;
import java.util.List;

/**
 * A request builder for creating multiple items
 */
public class CreateCollectionRequestBuilder<T extends ApiObject<ID>,ID extends Serializable>  extends CreateRequestBuilder<T,ID>
{
    /**
     * Create the request builder using the specified parameters
     * @param version The entity version
     * @param objectType The API type
     * @param entityName The entity name
     * @param entries The entries to create
     */
    public CreateCollectionRequestBuilder(int version, Class<T> objectType, String entityName, List<T> entries)
    {
        super(version, objectType, entityName, entries);
    }

    @Override
    public CreateCollectionRequestBuilder<T, ID> returnFields(String... fields)
    {
        super.returnFields(fields);
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

    /**
     * Execute the request using the supplied client
     * @param client the client
     * @return The request
     */
    public List<T> execute(HyperionClient client)
    {
        EntityList<T> response = client.create(build());
        return response.getEntries();
    }
}
