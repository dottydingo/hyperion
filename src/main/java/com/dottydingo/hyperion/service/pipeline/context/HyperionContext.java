package com.dottydingo.hyperion.service.pipeline.context;

import com.dottydingo.hyperion.service.configuration.EntityPlugin;
import com.dottydingo.hyperion.service.endpoint.HttpMethod;
import com.dottydingo.service.endpoint.context.EndpointContext;
import com.dottydingo.service.endpoint.context.EndpointRequest;
import com.dottydingo.service.endpoint.context.EndpointResponse;

/**
 */
public class HyperionContext extends EndpointContext<EndpointRequest,EndpointResponse>
{
    private EntityPlugin entityPlugin;
    private Integer version;
    private HttpMethod httpMethod;

    public EntityPlugin getEntityPlugin()
    {
        return entityPlugin;
    }

    public void setEntityPlugin(EntityPlugin entityPlugin)
    {
        this.entityPlugin = entityPlugin;
    }

    public Integer getVersion()
    {
        return version;
    }

    public void setVersion(Integer version)
    {
        this.version = version;
    }

    public HttpMethod getHttpMethod()
    {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod)
    {
        this.httpMethod = httpMethod;
    }
}
