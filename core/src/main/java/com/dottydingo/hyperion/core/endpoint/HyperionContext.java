package com.dottydingo.hyperion.core.endpoint;

import com.dottydingo.hyperion.core.registry.ApiVersionPlugin;
import com.dottydingo.hyperion.core.registry.EntityPlugin;
import com.dottydingo.hyperion.core.endpoint.pipeline.auth.AuthorizationContext;
import com.dottydingo.service.endpoint.context.EndpointContext;
import com.dottydingo.service.endpoint.context.UserContext;

import java.util.Locale;

/**
 */
public class HyperionContext extends EndpointContext<HyperionRequest,HyperionResponse,UserContext>
{
    private EntityPlugin entityPlugin;
    private Integer version;
    private HttpMethod effectiveMethod;
    private HttpMethod requestMethod;
    private ApiVersionPlugin versionPlugin;
    private String id;
    private boolean history;
    private Object result;
    private AuthorizationContext authorizationContext;
    private boolean showErrorDetail;
    private Locale locale;

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

    public HttpMethod getEffectiveMethod()
    {
        return effectiveMethod;
    }

    public void setEffectiveMethod(HttpMethod effectiveMethod)
    {
        this.effectiveMethod = effectiveMethod;
    }

    public HttpMethod getRequestMethod()
    {
        return requestMethod;
    }

    public void setRequestMethod(HttpMethod requestMethod)
    {
        this.requestMethod = requestMethod;
    }

    public void setVersionPlugin(ApiVersionPlugin versionPlugin)
    {
        this.versionPlugin = versionPlugin;
    }

    public ApiVersionPlugin getVersionPlugin()
    {
        return versionPlugin;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public boolean isHistory()
    {
        return history;
    }

    public void setHistory(boolean history)
    {
        this.history = history;
    }

    public Object getResult()
    {
        return result;
    }

    public void setResult(Object result)
    {
        this.result = result;
    }

    public AuthorizationContext getAuthorizationContext()
    {
        return authorizationContext;
    }

    public void setAuthorizationContext(AuthorizationContext authorizationContext)
    {
        this.authorizationContext = authorizationContext;
    }


    public void setShowErrorDetail(boolean showErrorDetail)
    {
        this.showErrorDetail = showErrorDetail;
    }

    public boolean getShowErrorDetail()
    {
        return showErrorDetail;
    }

    public Locale getLocale()
    {
        return locale;
    }

    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }
}
