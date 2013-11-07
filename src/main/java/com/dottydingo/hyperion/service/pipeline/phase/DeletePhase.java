package com.dottydingo.hyperion.service.pipeline.phase;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.service.configuration.ApiVersionPlugin;
import com.dottydingo.hyperion.service.configuration.EntityPlugin;
import com.dottydingo.hyperion.api.DeleteResponse;
import com.dottydingo.hyperion.service.model.PersistentObject;
import com.dottydingo.hyperion.service.context.HyperionContext;
import com.dottydingo.hyperion.service.persistence.PersistenceContext;
import com.dottydingo.service.endpoint.context.EndpointRequest;
import com.dottydingo.service.endpoint.context.EndpointResponse;

import java.util.List;

/**
 */
public class DeletePhase extends BasePersistencePhase<HyperionContext>
{
    @Override
    protected void executePhase(HyperionContext phaseContext) throws Exception
    {
        EndpointRequest request = phaseContext.getEndpointRequest();
        EndpointResponse response = phaseContext.getEndpointResponse();

        ApiVersionPlugin<ApiObject,PersistentObject> apiVersionPlugin = phaseContext.getVersionPlugin();
        EntityPlugin plugin = phaseContext.getEntityPlugin();

        PersistenceContext persistenceContext = buildPersistenceContext(phaseContext);

        List ids = plugin.getKeyConverter().covertKeys(phaseContext.getId());
        int deleted = plugin.getPersistenceOperations().deleteItem(ids, persistenceContext);

        processChangeEvents(phaseContext,persistenceContext);
        DeleteResponse deleteResponse = new DeleteResponse();
        deleteResponse.setCount(deleted);
        phaseContext.setResult(deleteResponse);

        response.setResponseCode(200);
    }
}
