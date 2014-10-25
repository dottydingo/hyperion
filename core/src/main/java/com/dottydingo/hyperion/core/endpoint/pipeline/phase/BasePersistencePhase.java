package com.dottydingo.hyperion.core.endpoint.pipeline.phase;

import com.dottydingo.hyperion.api.exception.BadParameterException;
import com.dottydingo.hyperion.api.exception.BadRequestException;
import com.dottydingo.hyperion.core.endpoint.HyperionContext;
import com.dottydingo.hyperion.core.endpoint.HyperionRequest;
import com.dottydingo.hyperion.core.key.KeyConverterException;
import com.dottydingo.hyperion.core.persistence.event.EntityChangeEvent;
import com.dottydingo.hyperion.core.persistence.event.EntityChangeListener;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.hyperion.core.registry.EntityPlugin;
import com.dottydingo.service.endpoint.context.MultiMap;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 */
public abstract class BasePersistencePhase extends BaseHyperionPhase
{
    protected static final String INVALID_INTEGER_PARAMETER = "ERROR_INVALID_INTEGER_PARAMETER";
    protected static final String ERROR_SINGLE_ID_REQUIRED = "ERROR_SINGLE_ID_REQUIRED";
    protected static final String BAD_START_PARAMETER = "ERROR_BAD_START_PARAMETER";
    protected static final String BAD_LIMIT_PARAMETER = "ERROR_BAD_LIMIT_PARAMETER";
    protected static final String ERROR_READING_REQUEST = "ERROR_READING_REQUEST";
    public static final String INVALID_ID = "ERROR_INVALID_ID";

    protected Set<String> buildFieldSet(String fields)
    {
        if(fields == null || fields.length() == 0)
            return null;

        String[] split = fields.split(",");
        Set<String> fieldSet = new LinkedHashSet<String>();
        for (String s : split)
        {
            fieldSet.add(s.trim());
        }

        return fieldSet;
    }

    protected Integer getIntegerParameter(String name, HyperionContext context)
    {
        String value = context.getEndpointRequest().getFirstParameter(name);
        if(value == null)
            return null;

        try
        {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException e)
        {
            throw new BadRequestException(messageSource.getErrorMessage(INVALID_INTEGER_PARAMETER,context.getLocale(),name));
        }
    }

    protected void processChangeEvents(HyperionContext phaseContext,PersistenceContext persistenceContext)
    {
        EntityPlugin entityPlugin = phaseContext.getEntityPlugin();
        if(!entityPlugin.hasEntityChangeListeners())
            return;

        List<EntityChangeListener> entityChangeListeners = entityPlugin.getEntityChangeListeners();
        for (EntityChangeListener entityChangeListener : entityChangeListeners)
        {
            for (EntityChangeEvent event : persistenceContext.getEntityChangeEvents())
            {
                entityChangeListener.processEntityChange(event);
            }
        }
    }

    protected PersistenceContext buildPersistenceContext(HyperionContext context)
    {
        EntityPlugin entityPlugin = context.getEntityPlugin();
        HyperionRequest endpointRequest = context.getEndpointRequest();

        PersistenceContext persistenceContext = new PersistenceContext();
        persistenceContext.setEntityPlugin(entityPlugin);
        persistenceContext.setEntity(entityPlugin.getEndpointName());
        persistenceContext.setHttpMethod(context.getEffectiveMethod());
        persistenceContext.setApiVersionPlugin(context.getVersionPlugin());
        persistenceContext.setUserContext(context.getUserContext());
        persistenceContext.setRequestedFields(buildFieldSet(endpointRequest.getFirstParameter("fields")));
        persistenceContext.setAuthorizationContext(context.getAuthorizationContext());
        persistenceContext.setLocale(context.getLocale());
        persistenceContext.setMessageSource(messageSource);

        Set<String> additionalParameters = entityPlugin.getAdditionalParameters();
        if(additionalParameters != null && additionalParameters.size() > 0)
            persistenceContext.setAdditionalParameters(endpointRequest.getParameterMap(additionalParameters));

        return persistenceContext;
    }

    protected List convertIds(HyperionContext phaseContext, EntityPlugin plugin)
    {
        try
        {
            return plugin.getKeyConverter().covertKeys(phaseContext.getId());
        }
        catch (KeyConverterException e)
        {
            throw new BadParameterException(messageSource.getErrorMessage(INVALID_ID,phaseContext.getLocale(),e.getValue()));
        }
    }
}
