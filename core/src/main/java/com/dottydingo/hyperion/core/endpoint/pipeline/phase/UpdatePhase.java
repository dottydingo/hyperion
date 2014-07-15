package com.dottydingo.hyperion.core.endpoint.pipeline.phase;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.exception.BadRequestException;
import com.dottydingo.hyperion.core.endpoint.marshall.MarshallingException;
import com.dottydingo.hyperion.core.registry.ApiVersionPlugin;
import com.dottydingo.hyperion.core.registry.EntityPlugin;
import com.dottydingo.hyperion.core.endpoint.marshall.EndpointMarshaller;
import com.dottydingo.hyperion.core.endpoint.marshall.RequestContext;
import com.dottydingo.hyperion.core.model.PersistentObject;
import com.dottydingo.hyperion.core.endpoint.HyperionContext;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.service.endpoint.context.EndpointRequest;
import com.dottydingo.service.endpoint.context.EndpointResponse;

import java.util.List;
import java.util.Set;

/**
 */
public class UpdatePhase extends BasePersistencePhase
{
    private EndpointMarshaller marshaller;

    public void setMarshaller(EndpointMarshaller marshaller)
    {
        this.marshaller = marshaller;
    }

    @Override
    protected void executePhase(HyperionContext phaseContext) throws Exception
    {
        EndpointRequest request = phaseContext.getEndpointRequest();
        EndpointResponse response = phaseContext.getEndpointResponse();

        ApiVersionPlugin<ApiObject,PersistentObject> apiVersionPlugin = phaseContext.getVersionPlugin();
        EntityPlugin plugin = phaseContext.getEntityPlugin();

        PersistenceContext persistenceContext = buildPersistenceContext(phaseContext);

        RequestContext<ApiObject> requestContext = null;
        try
        {
            requestContext = marshaller.unmarshallWithContext(request.getInputStream(), apiVersionPlugin.getApiClass());
        }
        catch (MarshallingException e)
        {
            throw new BadRequestException(messageSource.getErrorMessage(ERROR_READING_REQUEST, phaseContext.getLocale(),
                    e.getMessage()),e);
        }
        ApiObject clientObject = requestContext.getRequestObject();

        List ids = convertIds(phaseContext, plugin);
        if(ids.size() != 1)
            throw new BadRequestException(messageSource.getErrorMessage(ERROR_SINGLE_ID_REQUIRED,phaseContext.getLocale()));

        persistenceContext.setProvidedFields(requestContext.getProvidedFields());

        Set<String> fieldSet = persistenceContext.getRequestedFields();
        if(fieldSet != null)
            fieldSet.add("id");

        ApiObject saved = plugin.getPersistenceOperations().updateItem(ids, clientObject, persistenceContext);

        processChangeEvents(phaseContext,persistenceContext);

        if(saved != null)
        {
            response.setResponseCode(200);
            phaseContext.setResult(saved);
        }
        else
            response.setResponseCode(304);
    }
}
