package com.dottydingo.hyperion.core.endpoint.pipeline.phase;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.core.registry.ApiVersionPlugin;
import com.dottydingo.hyperion.core.registry.EntityPlugin;
import com.dottydingo.hyperion.api.DeleteResponse;
import com.dottydingo.hyperion.core.model.PersistentObject;
import com.dottydingo.hyperion.core.endpoint.HyperionContext;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.service.endpoint.context.EndpointRequest;
import com.dottydingo.service.endpoint.context.EndpointResponse;

import java.io.Serializable;
import java.util.List;

/**
 */
public class DeletePhase extends BasePersistencePhase
{
    @Override
    protected void executePhase(HyperionContext phaseContext) throws Exception
    {
        EndpointRequest request = phaseContext.getEndpointRequest();
        EndpointResponse response = phaseContext.getEndpointResponse();

        ApiVersionPlugin<ApiObject<Serializable>,PersistentObject<Serializable>,Serializable> apiVersionPlugin = phaseContext.getVersionPlugin();
        EntityPlugin plugin = phaseContext.getEntityPlugin();

        PersistenceContext persistenceContext = buildPersistenceContext(phaseContext);

        List ids = convertIds(phaseContext, plugin);
        int deleted = plugin.getPersistenceOperations().deleteItem(ids, persistenceContext);

        processChangeEvents(phaseContext,persistenceContext);
        DeleteResponse deleteResponse = new DeleteResponse();
        deleteResponse.setCount(deleted);
        phaseContext.setResult(deleteResponse);

        response.setResponseCode(200);
    }

}
