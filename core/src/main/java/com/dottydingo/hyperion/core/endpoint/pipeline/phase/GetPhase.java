package com.dottydingo.hyperion.core.endpoint.pipeline.phase;

import com.dottydingo.hyperion.core.registry.EntityPlugin;
import com.dottydingo.hyperion.api.EntityResponse;
import com.dottydingo.hyperion.core.endpoint.HyperionContext;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.service.endpoint.context.EndpointResponse;

import java.util.List;

/**
 */
public class GetPhase extends BasePersistencePhase
{
    @Override
    protected void executePhase(HyperionContext phaseContext) throws Exception
    {
        EntityPlugin plugin = phaseContext.getEntityPlugin();
        List ids = convertIds(phaseContext, plugin);

        PersistenceContext persistenceContext = buildPersistenceContext(phaseContext);

        List converted = plugin.getPersistenceOperations().findByIds(ids, persistenceContext);

        EntityResponse entityResponse = new EntityResponse();
        entityResponse.setEntries(converted);
        entityResponse.setResponseCount(converted.size());
        entityResponse.setStart(1);
        entityResponse.setTotalCount(new Long(converted.size()));

        phaseContext.setResult(entityResponse);

        EndpointResponse response = phaseContext.getEndpointResponse();
        response.setResponseCode(200);
    }
}
