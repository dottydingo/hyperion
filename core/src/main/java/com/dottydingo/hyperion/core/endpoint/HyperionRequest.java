package com.dottydingo.hyperion.core.endpoint;

import com.dottydingo.service.endpoint.context.EndpointRequest;
import com.dottydingo.service.endpoint.context.MultiMap;

import java.util.Collection;

/**
 */
public class HyperionRequest extends EndpointRequest
{
    protected String resourceUri;

    public String getResourceUri()
    {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri)
    {
        this.resourceUri = resourceUri;
    }

    public MultiMap getParameterMap(Collection<String> parameterNames)
    {
        return parameters.filter(parameterNames);
    }
}
