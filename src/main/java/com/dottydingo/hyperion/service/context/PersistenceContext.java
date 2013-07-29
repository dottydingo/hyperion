package com.dottydingo.hyperion.service.context;

import com.dottydingo.hyperion.service.configuration.ApiVersionPlugin;
import com.dottydingo.hyperion.service.configuration.EntityPlugin;
import com.dottydingo.hyperion.service.endpoint.HttpMethod;

import java.util.Set;

/**
 *  Persistence Context
 */
public class PersistenceContext
{
    private String entity;
    private Set<String> requestedFields;
    private EntityPlugin entityPlugin;
    private ApiVersionPlugin apiVersionPlugin;
    private HttpMethod httpMethod;
    private UserContext userContext;
    private WriteContext writeContext;

    public String getEntity()
    {
        return entity;
    }

    public void setEntity(String entity)
    {
        this.entity = entity;
    }

    public Set<String> getRequestedFields()
    {
        return requestedFields;
    }

    public void setRequestedFields(Set<String> requestedFields)
    {
        this.requestedFields = requestedFields;
    }

    public EntityPlugin getEntityPlugin()
    {
        return entityPlugin;
    }

    public void setEntityPlugin(EntityPlugin entityPlugin)
    {
        this.entityPlugin = entityPlugin;
    }

    public ApiVersionPlugin getApiVersionPlugin()
    {
        return apiVersionPlugin;
    }

    public void setApiVersionPlugin(ApiVersionPlugin apiVersionPlugin)
    {
        this.apiVersionPlugin = apiVersionPlugin;
    }

    public HttpMethod getHttpMethod()
    {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod)
    {
        this.httpMethod = httpMethod;
    }

    public UserContext getUserContext()
    {
        return userContext;
    }

    public void setUserContext(UserContext userContext)
    {
        this.userContext = userContext;
    }

    public WriteContext getWriteContext()
    {
        return writeContext;
    }

    public void setWriteContext(WriteContext writeContext)
    {
        this.writeContext = writeContext;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        PersistenceContext ctx = new PersistenceContext();
        ctx.setApiVersionPlugin(apiVersionPlugin);
        ctx.setEntity(entity);
        ctx.setEntityPlugin(entityPlugin);
        ctx.setHttpMethod(httpMethod);
        ctx.setRequestedFields(requestedFields);
        ctx.setUserContext(userContext);
        ctx.setWriteContext(writeContext);

        return ctx;
    }
}
