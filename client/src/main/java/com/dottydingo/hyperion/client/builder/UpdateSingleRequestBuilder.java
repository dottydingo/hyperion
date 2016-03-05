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
 * A request builder for updating a single item
 */
public class UpdateSingleRequestBuilder<T extends ApiObject<ID>,ID extends Serializable>
        extends UpdateRequestBuilder<T,ID>
{

    /**
     * Create the request builder using the specified parameters
     * @param version The entity version
     * @param objectType The API type
     * @param entityName The entity name
     * @param entry The entry to update
     */
    public UpdateSingleRequestBuilder(int version, Class<T> objectType, String entityName, T entry)
    {
        super(version, objectType, entityName, Collections.singletonList(entry));

    }

    @Override
    public UpdateSingleRequestBuilder<T, ID> returnFields(String... fields)
    {
        super.returnFields(fields);
        return this;
    }

    @Override
    public UpdateSingleRequestBuilder<T, ID> addParameter(String name, String value)
    {
        super.addParameter(name, value);
        return this;
    }

    @Override
    public UpdateSingleRequestBuilder<T, ID> setParameter(String name, String value)
    {
        super.setParameter(name, value);
        return this;
    }

    @Override
    public UpdateSingleRequestBuilder<T, ID> addHeader(String name, String value)
    {
        super.addHeader(name, value);
        return this;
    }

    @Override
    public UpdateSingleRequestBuilder<T, ID> setHeader(String name, String value)
    {
        super.setHeader(name, value);
        return this;
    }

    @Override
    public UpdateSingleRequestBuilder<T, ID> withHeaderFactory(HeaderFactory headerFactory)
    {
        super.withHeaderFactory(headerFactory);
        return this;
    }

    @Override
    public UpdateSingleRequestBuilder<T, ID> withParameterFactory(ParameterFactory parameterFactory)
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
        EntityList<T> entityResponse = client.update(build());
        List<T> entries = entityResponse.getEntries();
        return entries.size() == 0 ? null : entries.get(0);
    }
}
