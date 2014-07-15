package com.dottydingo.hyperion.client;

import com.dottydingo.hyperion.api.ApiObject;

/**
 */
public class Request<T extends ApiObject>
{
    private MultiMap headers = new MultiMap();
    private MultiMap parameters = new MultiMap();
    private Class<T> entityType;
    private String entityName;
    private RequestMethod requestMethod;
    private String path;
    private T requestBody;

    public MultiMap getHeaders()
    {
        return headers;
    }

    public void setHeaders(MultiMap headers)
    {
        this.headers = headers;
    }

    public MultiMap getParameters()
    {
        return parameters;
    }

    public void setParameters(MultiMap parameters)
    {
        this.parameters = parameters;
    }

    public RequestMethod getRequestMethod()
    {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod)
    {
        this.requestMethod = requestMethod;
    }

    public Class<T> getEntityType()
    {
        return entityType;
    }

    public void setEntityType(Class<T> entityType)
    {
        this.entityType = entityType;
    }

    public String getEntityName()
    {
        return entityName;
    }

    public void setEntityName(String entityName)
    {
        this.entityName = entityName;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public T getRequestBody()
    {
        return requestBody;
    }

    public void setRequestBody(T requestBody)
    {
        this.requestBody = requestBody;
    }
}
