package com.dottydingo.hyperion.service.pipeline.phase;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.exception.BadRequestException;
import com.dottydingo.hyperion.service.configuration.ApiVersionPlugin;
import com.dottydingo.hyperion.service.configuration.EntityPlugin;
import com.dottydingo.hyperion.service.context.RequestContext;
import com.dottydingo.hyperion.service.endpoint.DeleteResponse;
import com.dottydingo.hyperion.service.marshall.EndpointMarshaller;
import com.dottydingo.hyperion.service.model.PersistentObject;
import com.dottydingo.hyperion.service.pipeline.context.HyperionContext;
import com.dottydingo.service.endpoint.context.EndpointRequest;
import com.dottydingo.service.endpoint.context.EndpointResponse;

import java.util.List;
import java.util.Set;

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

        RequestContext persistenceContext = buildPersistenceContext(phaseContext);

        List ids = plugin.getKeyConverter().covertKeys(phaseContext.getId());
        int deleted = plugin.getPersistenceOperations().deleteItem(ids, persistenceContext);

        DeleteResponse deleteResponse = new DeleteResponse();
        deleteResponse.setCount(deleted);
        phaseContext.setResult(deleteResponse);

        response.setResponseCode(200);
    }
}
