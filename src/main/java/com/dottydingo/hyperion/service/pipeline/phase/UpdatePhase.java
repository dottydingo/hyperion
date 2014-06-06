package com.dottydingo.hyperion.service.pipeline.phase;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.exception.BadRequestException;
import com.dottydingo.hyperion.service.configuration.ApiVersionPlugin;
import com.dottydingo.hyperion.service.configuration.EntityPlugin;
import com.dottydingo.hyperion.service.marshall.EndpointMarshaller;
import com.dottydingo.hyperion.service.marshall.RequestContext;
import com.dottydingo.hyperion.service.model.PersistentObject;
import com.dottydingo.hyperion.service.context.HyperionContext;
import com.dottydingo.hyperion.service.persistence.PersistenceContext;
import com.dottydingo.service.endpoint.context.EndpointRequest;
import com.dottydingo.service.endpoint.context.EndpointResponse;

import java.util.List;
import java.util.Set;

/**
 */
public class UpdatePhase extends BasePersistencePhase<HyperionContext>
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

        RequestContext<ApiObject> requestContext = marshaller.unmarshallWithContext(request.getInputStream(), apiVersionPlugin.getApiClass());
        ApiObject clientObject = requestContext.getRequestObject();

        List ids = plugin.getKeyConverter().covertKeys(phaseContext.getId());
        if(ids.size() != 1)
            throw new BadRequestException("A single id must be provided for an update.");

        persistenceContext.setProvidedFields(requestContext.getSetFields());

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
