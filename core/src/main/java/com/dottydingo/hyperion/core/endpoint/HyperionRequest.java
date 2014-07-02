package com.dottydingo.hyperion.core.endpoint;

import com.dottydingo.service.endpoint.context.EndpointRequest;

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
}
