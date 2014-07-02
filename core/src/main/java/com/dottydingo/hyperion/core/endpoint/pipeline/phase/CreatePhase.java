package com.dottydingo.hyperion.core.endpoint.pipeline.phase;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.core.registry.ApiVersionPlugin;
import com.dottydingo.hyperion.core.registry.EntityPlugin;
import com.dottydingo.hyperion.core.endpoint.marshall.EndpointMarshaller;
import com.dottydingo.hyperion.core.model.PersistentObject;
import com.dottydingo.hyperion.core.endpoint.HyperionContext;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.hyperion.core.persistence.WriteContext;
import com.dottydingo.service.endpoint.context.EndpointRequest;
import com.dottydingo.service.endpoint.context.EndpointResponse;

import java.util.Set;

/**
 */
public class CreatePhase extends BasePersistencePhase<HyperionContext>
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

        ApiObject clientObject = marshaller.unmarshall(request.getInputStream(),apiVersionPlugin.getApiClass());
        clientObject.setId(null);

        PersistenceContext persistenceContext = buildPersistenceContext(phaseContext);
        Set<String> fieldSet = persistenceContext.getRequestedFields();
        if(fieldSet != null)
            fieldSet.add("id");

        ApiObject saved = plugin.getPersistenceOperations().createOrUpdateItem(clientObject, persistenceContext);
        if(saved != null)
        {
            processChangeEvents(phaseContext,persistenceContext);
            if(persistenceContext.getWriteContext() == WriteContext.create)
            {
                response.setResponseCode(201);
                String location = String.format("%s%s/%s",request.getBaseUrl(),request.getRequestUri(),saved.getId());
                response.setHeader("Location", location);
            }
            else
            {
                response.setResponseCode(200);
            }
            phaseContext.setResult(saved);
        }
        else
            response.setResponseCode(304);

    }
}