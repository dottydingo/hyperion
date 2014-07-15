package com.dottydingo.hyperion.client.builder;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.client.HeaderFactory;
import com.dottydingo.hyperion.client.MultiMap;
import com.dottydingo.hyperion.client.ParameterFactory;
import com.dottydingo.hyperion.client.Request;

import java.io.Serializable;
import java.util.List;

/**
 */
public abstract class RequestBuilder<T extends ApiObject<ID>,ID extends Serializable>
{
    private MultiMap headers = new MultiMap();
    private MultiMap parameters = new MultiMap();
    private HeaderFactory headerFactory;
    private ParameterFactory parameterFactory;
    protected int version;
    protected Class<T> objectType;
    protected String entityName;

    protected RequestBuilder(int version, Class<T> objectType, String entityName)
    {
        this.version = version;
        this.objectType = objectType;
        this.entityName = entityName;
    }

    public RequestBuilder<T,ID> addParameter(String name,String value)
    {
        parameters.add(name, value);
        return this;
    }

    public RequestBuilder<T,ID> setParameter(String name,String value)
    {
        parameters.set(name, value);
        return this;
    }

    public RequestBuilder<T,ID> addHeader(String name,String value)
    {
        headers.add(name, value);
        return this;
    }

    public RequestBuilder<T,ID> setHeader(String name,String value)
    {
        headers.add(name, value);
        return this;
    }

    public RequestBuilder<T,ID> withHeaderFactory(HeaderFactory headerFactory)
    {
        this.headerFactory = headerFactory;
        return this;
    }

    public RequestBuilder<T,ID> withParameterFactory(ParameterFactory parameterFactory)
    {
        this.parameterFactory = parameterFactory;
        return this;
    }

    public Request<T> build()
    {
        MultiMap headers = resolveHeaders();
        MultiMap parameters = resolveParameters();

        parameters.add("version",Integer.toString(version));

        Request<T> request = new Request<T>();
        request.setEntityName(entityName);
        request.setEntityType(objectType);
        request.setHeaders(headers);
        request.setParameters(parameters);

        return request;
    }

    protected MultiMap resolveHeaders()
    {
        MultiMap resolved = null;
        if(headerFactory != null)
            resolved = headerFactory.getHeaders();

        if(resolved != null)
            resolved = resolved.merge(headers);
        else
            resolved = headers;

        return resolved;
    }

    protected MultiMap resolveParameters()
    {
        MultiMap resolved = null;
        if(parameterFactory != null)
            resolved = parameterFactory.getParameters();

        if(resolved != null)
            resolved = resolved.merge(parameters);
        else
            resolved = parameters;

        return resolved;
    }

    protected String join(Object[] values)
    {
        StringBuilder sb = new StringBuilder(100);
        if(values.length == 0)
            return sb.toString();

        sb.append(values[0]);

        if(values.length == 1)
            return sb.toString();

        for (int i = 1; i < values.length; i++)
        {
            sb.append(",").append(values[i]);
        }

        return sb.toString();
    }

    protected String join(List<String> values)
    {
        StringBuilder sb = new StringBuilder(100);
        if(values.size() == 0)
            return sb.toString();

        sb.append(values.get(0));

        if(values.size() == 1)
            return sb.toString();

        for (int i = 1; i < values.size(); i++)
        {
            sb.append(",").append(values.get(i));
        }

        return sb.toString();
    }
}
