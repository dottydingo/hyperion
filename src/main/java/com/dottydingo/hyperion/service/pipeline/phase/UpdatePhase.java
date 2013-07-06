package com.dottydingo.hyperion.service.pipeline.phase;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.exception.BadRequestException;
import com.dottydingo.hyperion.service.configuration.ApiVersionPlugin;
import com.dottydingo.hyperion.service.configuration.EntityPlugin;
import com.dottydingo.hyperion.service.context.RequestContext;
import com.dottydingo.hyperion.service.marshall.EndpointMarshaller;
import com.dottydingo.hyperion.service.model.PersistentObject;
import com.dottydingo.hyperion.service.pipeline.context.HyperionContext;
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

        RequestContext persistenceContext = buildPersistenceContext(phaseContext);

        ApiObject clientObject = marshaller.unmarshall(request.getInputStream(), apiVersionPlugin.getApiClass());

        List ids = plugin.getKeyConverter().covertKeys(phaseContext.getId());
        if(ids.size() != 1)
            throw new BadRequestException("A single id must be provided for an update.");

        Set<String> fieldSet = persistenceContext.getRequestedFields();
        if(fieldSet != null)
            fieldSet.add("id");

        ApiObject saved = plugin.getPersistenceOperations().updateItem(ids, clientObject, persistenceContext);

        if(saved != null)
        {
            response.setResponseCode(200);
            phaseContext.setResult(saved);
        }
        else
            response.setResponseCode(304);
    }
}
