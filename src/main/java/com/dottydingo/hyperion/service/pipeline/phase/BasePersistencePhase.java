package com.dottydingo.hyperion.service.pipeline.phase;

import com.dottydingo.hyperion.exception.BadRequestException;
import com.dottydingo.hyperion.service.context.PersistenceContext;
import com.dottydingo.hyperion.service.context.HyperionContext;
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

    protected PersistenceContext buildPersistenceContext(C context)
    {
        PersistenceContext persistenceContext = new PersistenceContext();
        persistenceContext.setEntityPlugin(context.getEntityPlugin());
        persistenceContext.setEntity(context.getEntityPlugin().getEndpointName());
        persistenceContext.setHttpMethod(context.getHttpMethod());
        persistenceContext.setApiVersionPlugin(context.getVersionPlugin());
        persistenceContext.setUserContext(context.getUserContext());
        persistenceContext.setRequestedFields(buildFieldSet(context.getEndpointRequest().getFirstParameter("fields")));

        return persistenceContext;
    }

}
