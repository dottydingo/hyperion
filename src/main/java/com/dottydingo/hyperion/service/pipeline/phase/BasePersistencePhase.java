package com.dottydingo.hyperion.service.pipeline.phase;

import com.dottydingo.hyperion.exception.BadRequestException;
import com.dottydingo.hyperion.service.context.HyperionContext;
import com.dottydingo.hyperion.service.persistence.EntityChangeEvent;
import com.dottydingo.hyperion.service.persistence.EntityChangeListener;
import com.dottydingo.hyperion.service.persistence.PersistenceContext;
import com.dottydingo.service.endpoint.context.EndpointRequest;
import com.dottydingo.service.endpoint.pipeline.AbstractEndpointPhase;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 */
public abstract class BasePersistencePhase<C extends HyperionContext> extends AbstractEndpointPhase<C>
{
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

    protected Integer getIntegerParameter(String name, EndpointRequest request)
    {
        String value = request.getFirstParameter(name);
        if(value == null)
            return null;

        try
        {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException e)
        {
            throw new BadRequestException(String.format("%s must be an integer.",name));
        }
    }

    protected void processChangeEvents(C phaseContext,PersistenceContext persistenceContext)
    {
        EntityChangeListener entityChangeListener = phaseContext.getEntityPlugin().getEntityChangeListener();
        if(entityChangeListener != null)
        {
            for (EntityChangeEvent event : persistenceContext.getEntityChangeEvents())
            {
                entityChangeListener.processEntityChange(event);
            }
        }
    }

    protected PersistenceContext buildPersistenceContext(C context)
    {
        PersistenceContext persistenceContext = new PersistenceContext();
        persistenceContext.setEntityPlugin(context.getEntityPlugin());
        persistenceContext.setEntity(context.getEntityPlugin().getEndpointName());
        persistenceContext.setHttpMethod(context.getEffectiveMethod());
        persistenceContext.setApiVersionPlugin(context.getVersionPlugin());
        persistenceContext.setUserContext(context.getUserContext());
        persistenceContext.setRequestedFields(buildFieldSet(context.getEndpointRequest().getFirstParameter("fields")));
        persistenceContext.setAuthorizationContext(context.getAuthorizationContext());
        persistenceContext.setLocale(context.getLocal());

        return persistenceContext;
    }

}
