package com.dottydingo.hyperion.core.persistence;

import com.dottydingo.hyperion.core.registry.ApiVersionPlugin;
import com.dottydingo.hyperion.core.registry.EntityPlugin;
import com.dottydingo.hyperion.core.endpoint.HttpMethod;
import com.dottydingo.hyperion.core.endpoint.pipeline.auth.AuthorizationContext;
import com.dottydingo.service.endpoint.context.UserContext;

import java.util.*;

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
    private Set<String> changedFields = new HashSet<String>();
    private List<EntityChangeEvent> entityChangeEvents = new ArrayList<EntityChangeEvent>();
    private AuthorizationContext authorizationContext;
    private Locale locale;
    private Set<String> providedFields = Collections.emptySet();

    public PersistenceContext()
    {
    }

    public PersistenceContext(PersistenceContext other)
    {
        this.entity = other.entity;
        this.requestedFields = other.requestedFields;
        this.entityPlugin = other.entityPlugin;
        this.apiVersionPlugin = other.apiVersionPlugin;
        this.httpMethod = other.httpMethod;
        this.userContext = other.userContext;
        this.writeContext = other.writeContext;
        this.dirty = other.dirty;
        this.currentTimestamp = other.currentTimestamp;
        this.changedFields = other.changedFields;
        this.entityChangeEvents = other.entityChangeEvents;
        this.authorizationContext = other.authorizationContext;
        this.locale = other.locale;
        this.providedFields = other.providedFields;
    }


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

    public void addChangedField(String fieldName)
    {
        changedFields.add(fieldName);
    }

    public Set<String> getChangedFields()
    {
        return changedFields;
    }

    public void setCurrentTimestamp(Date currentTimestamp)
    {
        this.currentTimestamp = currentTimestamp;
    }

    public Date getCurrentTimestamp()
    {
        return currentTimestamp;
    }

    public void addEntityChangeEvent(EntityChangeEvent entityChangeEvent)
    {
        entityChangeEvents.add(entityChangeEvent);
    }

    public List<EntityChangeEvent> getEntityChangeEvents()
    {
        return entityChangeEvents;
    }

    public AuthorizationContext getAuthorizationContext()
    {
        return authorizationContext;
    }

    public void setAuthorizationContext(AuthorizationContext authorizationContext)
    {
        this.authorizationContext = authorizationContext;
    }

    public Locale getLocale()
    {
        return locale;
    }

    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }


    public Set<String> getProvidedFields()
    {
        return providedFields;
    }

    public void setProvidedFields(Set<String> providedFields)
    {
        this.providedFields = providedFields;
    }

    public boolean isFieldProvided(String fieldName)
    {
        return providedFields.contains(fieldName);
    }

    @Deprecated
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return new PersistenceContext(this);
    }


}
