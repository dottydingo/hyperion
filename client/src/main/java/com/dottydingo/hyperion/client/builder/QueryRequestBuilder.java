package com.dottydingo.hyperion.client.builder;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.EntityResponse;
import com.dottydingo.hyperion.client.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class QueryRequestBuilder<T extends ApiObject<ID>,ID extends Serializable> extends RequestBuilder<T,ID>
{
    private List<String> sorts = new ArrayList<String>();

    public QueryRequestBuilder(int version, Class<T> objectType, String entityName,String query)
    {
        this(version, objectType, entityName);
        setParameter("query",query);
    }

    public QueryRequestBuilder(int version, Class<T> objectType, String entityName)
    {
        super(version, objectType, entityName);
    }

    public QueryRequestBuilder<T, ID> returnFields(String... fields)
    {
        setParameter("fields",join(fields));
        return this;
    }

    public QueryRequestBuilder<T, ID> start(long start)
    {
        setParameter("start",Long.toString(start));
        return this;
    }

    public QueryRequestBuilder<T, ID> limit(long limit)
    {
        setParameter("limit",Long.toString(limit));
        return this;
    }

    public QueryRequestBuilder<T, ID> addAscendingSort(String field)
    {
        sorts.add(field);
        return this;
    }

    public QueryRequestBuilder<T, ID> addDescendingSort(String field)
    {
        sorts.add(field + ":desc");
        return this;
    }

    @Override
    public QueryRequestBuilder<T, ID> addParameter(String name, String value)
    {
        super.addParameter(name, value);
        return this;
    }

    @Override
    public QueryRequestBuilder<T, ID> setParameter(String name, String value)
    {
        super.setParameter(name, value);
        return this;
    }

    @Override
    public QueryRequestBuilder<T, ID> addHeader(String name, String value)
    {
        super.addHeader(name, value);
        return this;
    }

    @Override
    public QueryRequestBuilder<T, ID> setHeader(String name, String value)
    {
        super.setHeader(name, value);
        return this;
    }

    @Override
    public QueryRequestBuilder<T, ID> withHeaderFactory(HeaderFactory headerFactory)
    {
        super.withHeaderFactory(headerFactory);
        return this;
    }

    @Override
    public QueryRequestBuilder<T, ID> withParameterFactory(ParameterFactory parameterFactory)
    {
        super.withParameterFactory(parameterFactory);
        return this;
    }

    @Override
    public Request<T> build()
    {
        setParameter("sort",join(sorts));

        Request<T> request = super.build();
        request.setRequestMethod(RequestMethod.GET);

        return request;
    }

    public EntityResponse<T> execute(HyperionClient client)
    {
        return client.query(build());
    }
}
