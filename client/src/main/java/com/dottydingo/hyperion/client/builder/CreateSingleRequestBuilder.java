package com.dottydingo.hyperion.client.builder;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.EntityList;
import com.dottydingo.hyperion.client.HeaderFactory;
import com.dottydingo.hyperion.client.HyperionClient;
import com.dottydingo.hyperion.client.ParameterFactory;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * A request builder for creating a single item
 */
public class CreateSingleRequestBuilder<T extends ApiObject<ID>,ID extends Serializable> extends CreateRequestBuilder<T,ID>
{
    /**
     * Create the request builder using the specified parameters
     * @param version The entity version
     * @param objectType The API type
     * @param entityName The entity name
     * @param entry The entry to create
     */
    public CreateSingleRequestBuilder(int version, Class<T> objectType, String entityName, T entry)
    {
        super(version, objectType, entityName, Collections.singletonList(entry));
    }

    @Override
    public CreateSingleRequestBuilder<T, ID> returnFields(String... fields)
    {
        super.returnFields(fields);
        return this;
    }

    @Override
    public CreateSingleRequestBuilder<T, ID> addParameter(String name, String value)
    {
        super.addParameter(name, value);
        return this;
    }

    @Override
    public CreateSingleRequestBuilder<T, ID> setParameter(String name, String value)
    {
        super.setParameter(name, value);
        return this;
    }

    @Override
    public CreateSingleRequestBuilder<T, ID> addHeader(String name, String value)
    {
        super.addHeader(name, value);
        return this;
    }

    @Override
    public CreateSingleRequestBuilder<T, ID> setHeader(String name, String value)
    {
        super.setHeader(name, value);
        return this;
    }

    @Override
    public CreateSingleRequestBuilder<T, ID> withHeaderFactory(HeaderFactory headerFactory)
    {
        super.withHeaderFactory(headerFactory);
        return this;
    }

    @Override
    public CreateSingleRequestBuilder<T, ID> withParameterFactory(ParameterFactory parameterFactory)
    {
        super.withParameterFactory(parameterFactory);
        return this;
    }

    /**
     * Execute the request using the supplied client
     * @param client the client
     * @return The request
     */
    public T execute(HyperionClient client)
    {
        EntityList<T> response = client.create(build());
        List<T> entries = response.getEntries();
        return entries.size() == 0 ? null : entries.get(0);
    }
}
