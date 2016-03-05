package com.dottydingo.hyperion.client.builder;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.client.HeaderFactory;
import com.dottydingo.hyperion.client.MultiMap;
import com.dottydingo.hyperion.client.ParameterFactory;
import com.dottydingo.hyperion.client.Request;

import java.io.Serializable;
import java.util.List;

/**
 * Base request builder
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

    /**
     * Create the request builder using the specified parameters
     * @param version The entity version
     * @param objectType The API type
     * @param entityName The entity name
     */
    protected RequestBuilder(int version, Class<T> objectType, String entityName)
    {
        this.version = version;
        this.objectType = objectType;
        this.entityName = entityName;
    }

    /**
     * Add a parameter to the request. Adds this value to any existing values for this name.
     * @param name The name of the parameter
     * @param value The value
     * @return The request builder
     */
    public RequestBuilder<T,ID> addParameter(String name,String value)
    {
        parameters.add(name, value);
        return this;
    }

    /**
     * Set a parameter on the request. Replaces any existing values for the name.
     * @param name The name of the parameter
     * @param value The value
     * @return The request builder
     */
    public RequestBuilder<T,ID> setParameter(String name,String value)
    {
        parameters.set(name, value);
        return this;
    }

    /**
     * Add a header to the request. Adds this value to any existing values for this name.
     * @param name The name of the header
     * @param value The value
     * @return The request builder
     */
    public RequestBuilder<T,ID> addHeader(String name,String value)
    {
        headers.add(name, value);
        return this;
    }

    /**
     * Set a header on the request. Replaces any existing values for the name.
     * @param name The name of the header
     * @param value The value
     * @return The request builder
     */
    public RequestBuilder<T,ID> setHeader(String name,String value)
    {
        headers.add(name, value);
        return this;
    }

    /**
     * Set a header factory to use with this request
     * @param headerFactory The header factory
     * @return The request builder
     */
    public RequestBuilder<T,ID> withHeaderFactory(HeaderFactory headerFactory)
    {
        this.headerFactory = headerFactory;
        return this;
    }

    /**
     * Set a parameter factory to use with this request
     * @param parameterFactory The parameter factory
     * @return The request builder
     */
    public RequestBuilder<T,ID> withParameterFactory(ParameterFactory parameterFactory)
    {
        this.parameterFactory = parameterFactory;
        return this;
    }

    /**
     * Build the request
     * @return The request
     */
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

    /**
     * Combine any locally defined headers with any headers from the header factory
     * @return The headers to use for this request
     */
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

    /**
     * Combine any locally defined parameters with any parameters from the parameter factory
     * @return The parameters to use for this request
     */
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

    /**
     * Joing the values together in a comma separated list
     * @param values The values to join
     * @return The combined values
     */
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

    /**
     * Joing the values together in a comma separated list
     * @param values The values to join
     * @return The combined values
     */
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
