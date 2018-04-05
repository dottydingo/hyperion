package com.dottydingo.hyperion.core.persistence;

import com.dottydingo.hyperion.core.message.HyperionMessageSource;
import com.dottydingo.hyperion.core.persistence.event.EntityChangeEvent;
import com.dottydingo.hyperion.core.registry.ApiVersionPlugin;
import com.dottydingo.hyperion.core.registry.EntityPlugin;
import com.dottydingo.hyperion.core.endpoint.HttpMethod;
import com.dottydingo.hyperion.core.endpoint.pipeline.auth.AuthorizationContext;
import com.dottydingo.service.endpoint.context.MultiMap;
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
    private Date currentTimestamp;
    private Map<ItemChangeKey,Set<String>> itemChangedFieldsMap = new HashMap<>();
    private List<EntityChangeEvent> entityChangeEvents = new ArrayList<EntityChangeEvent>();
    private AuthorizationContext authorizationContext;
    private Locale locale;
    private HyperionMessageSource messageSource;
    private Map<Object,Set<String>> providedFields = Collections.emptyMap();
    private MultiMap additionalParameters = new MultiMap();
    private Map<String,Object> contextItemMap = new HashMap<>();

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
        this.currentTimestamp = other.currentTimestamp;
        this.itemChangedFieldsMap = other.itemChangedFieldsMap;
        this.entityChangeEvents = other.entityChangeEvents;
        this.authorizationContext = other.authorizationContext;
        this.locale = other.locale;
        this.messageSource = other.messageSource;
        this.providedFields = other.providedFields;
        this.additionalParameters = other.additionalParameters;
        this.contextItemMap = other.contextItemMap;
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


    public void addChangedField(String entity,Object id,String fieldName)
    {
        ItemChangeKey key = new ItemChangeKey(entity, id);
        Set<String> fields = itemChangedFieldsMap.get(key);
        if(fields == null)
        {
            fields = new HashSet<>();
            itemChangedFieldsMap.put(key,fields);
        }
        fields.add(fieldName);
    }

    public Set<String> getChangedFields(String entity,Object id)
    {
        ItemChangeKey key = new ItemChangeKey(entity, id);
        Set<String> fields = itemChangedFieldsMap.get(key);
        if(fields == null)
            return Collections.emptySet();

        return fields;
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


    public void setProvidedFields(Map<Object, Set<String>> providedFields)
    {
        this.providedFields = providedFields;
    }

    public boolean isFieldProvided(Object item, String fieldName)
    {
        if(providedFields == null)
            return false;

        Set<String> fields = providedFields.get(item);
        if(fields != null)
            return fields.contains(fieldName);

        return false;
    }

    public HyperionMessageSource getMessageSource()
    {
        return messageSource;
    }

    public void setMessageSource(HyperionMessageSource messageSource)
    {
        this.messageSource = messageSource;
    }

    public MultiMap getAdditionalParameters()
    {
        return additionalParameters;
    }

    public void setAdditionalParameters(MultiMap additionalParameters)
    {
        this.additionalParameters = additionalParameters;
    }

    public void putContextItem(String key, Object value)
    {
        contextItemMap.put(key,value);
    }

    public <T> T getContextItem(String key)
    {
        return (T) contextItemMap.get(key);
    }
}
