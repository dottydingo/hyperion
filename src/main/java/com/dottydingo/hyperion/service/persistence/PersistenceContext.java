package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.service.configuration.ApiVersionPlugin;
import com.dottydingo.hyperion.service.configuration.EntityPlugin;
import com.dottydingo.hyperion.service.endpoint.HttpMethod;
import com.dottydingo.hyperion.service.pipeline.auth.UserContext;

import java.util.Date;
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
    private boolean dirty = false;
    private Date currentTimestamp;

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

    public void setDirty()
    {
        this.dirty = true;
    }

    public boolean isDirty()
    {
        return dirty;
    }

    public void setCurrentTimestamp(Date currentTimestamp)
    {
        this.currentTimestamp = currentTimestamp;
    }

    public Date getCurrentTimestamp()
    {
        return currentTimestamp;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        PersistenceContext ctx = new PersistenceContext();
        ctx.apiVersionPlugin = this.apiVersionPlugin;
        ctx.entity = this.entity;
        ctx.entityPlugin = this.entityPlugin;
        ctx.httpMethod = this.httpMethod;
        ctx.requestedFields = this.requestedFields;
        ctx.userContext = this.userContext;
        ctx.writeContext = this.writeContext;
        ctx.dirty = this.dirty;
        ctx.currentTimestamp = this.currentTimestamp;

        return ctx;
    }


}
