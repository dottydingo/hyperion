package com.dottydingo.hyperion.client;

import com.dottydingo.hyperion.api.ApiObject;

/**
 * A data service request
 */
public class Request<T extends ApiObject>
{
    private MultiMap headers = new MultiMap();
    private MultiMap parameters = new MultiMap();
    private Class<T> entityType;
    private String entityName;
    private RequestMethod requestMethod;
    private String path;
    private Object requestBody;

    /**
     * Return the headers to use on the request
     * @return The headers
     */
    public MultiMap getHeaders()
    {
        return headers;
    }

    /**
     * Set the headers to use on the request
     * @param headers The headers
     */
    public void setHeaders(MultiMap headers)
    {
        this.headers = headers;
    }

    /**
     * Return the parameters to use on the request
     * @return The parameters
     */
    public MultiMap getParameters()
    {
        return parameters;
    }

    /**
     * Set the parameters to use on the request
     * @param parameters The parameters
     */
    public void setParameters(MultiMap parameters)
    {
        this.parameters = parameters;
    }

    /**
     * Return the request method
     * @return The request method
     */
    public RequestMethod getRequestMethod()
    {
        return requestMethod;
    }

    /**
     * Set the request method
     * @param requestMethod The request method
     */
    public void setRequestMethod(RequestMethod requestMethod)
    {
        this.requestMethod = requestMethod;
    }

    /**
     * Return the entity type for this request
     * @return The entity type
     */
    public Class<T> getEntityType()
    {
        return entityType;
    }

    /**
     * Set the entity type for this request
     * @param entityType The entity type
     */
    public void setEntityType(Class<T> entityType)
    {
        this.entityType = entityType;
    }

    /**
     * Return the entity name for this request
     * @return The entity name
     */
    public String getEntityName()
    {
        return entityName;
    }

    /**
     * Set the entity name for this request
     * @param entityName The entity name
     */
    public void setEntityName(String entityName)
    {
        this.entityName = entityName;
    }

    /**
     * Return the URI path for this request
     * @return the path
     */
    public String getPath()
    {
        return path;
    }

    /**
     * Set the URL path for this request
     * @param path The path
     */
    public void setPath(String path)
    {
        this.path = path;
    }

    /**
     * Set the request body for this request
     * @return The request body
     */
    public Object getRequestBody()
    {
        return requestBody;
    }

    /**
     * Return the request body for this request
     * @param requestBody The request body
     */
    public void setRequestBody(Object requestBody)
    {
        this.requestBody = requestBody;
    }
}
