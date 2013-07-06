package com.dottydingo.hyperion.service.pipeline.context;

import com.dottydingo.hyperion.service.configuration.ApiVersionPlugin;
import com.dottydingo.hyperion.service.configuration.EntityPlugin;
import com.dottydingo.hyperion.service.context.UserContext;
import com.dottydingo.hyperion.service.endpoint.HttpMethod;
import com.dottydingo.service.endpoint.context.EndpointContext;
import com.dottydingo.service.endpoint.context.EndpointRequest;
import com.dottydingo.service.endpoint.context.EndpointResponse;
import com.dottydingo.service.endpoint.status.ContextStatus;

/**
 */
public class HyperionContext extends EndpointContext<EndpointRequest,EndpointResponse,ContextStatus>
{
    private EntityPlugin entityPlugin;
    private Integer version;
    private HttpMethod httpMethod;
    private ApiVersionPlugin versionPlugin;
    private UserContext userContext;
    private String id;
    private boolean audit;
    private Object result;

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

    public void setVersionPlugin(ApiVersionPlugin versionPlugin)
    {
        this.versionPlugin = versionPlugin;
    }

    public ApiVersionPlugin getVersionPlugin()
    {
        return versionPlugin;
    }

    public UserContext getUserContext()
    {
        return userContext;
    }

    public void setUserContext(UserContext userContext)
    {
        this.userContext = userContext;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public boolean isAudit()
    {
        return audit;
    }

    public void setAudit(boolean audit)
    {
        this.audit = audit;
    }

    public Object getResult()
    {
        return result;
    }

    public void setResult(Object result)
    {
        this.result = result;
    }
}
