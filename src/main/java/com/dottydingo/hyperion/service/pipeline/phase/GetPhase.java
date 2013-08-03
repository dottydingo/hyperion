package com.dottydingo.hyperion.service.pipeline.phase;

import com.dottydingo.hyperion.service.configuration.EntityPlugin;
import com.dottydingo.hyperion.service.endpoint.EntityResponse;
import com.dottydingo.hyperion.service.context.HyperionContext;
import com.dottydingo.hyperion.service.persistence.PersistenceContext;
import com.dottydingo.service.endpoint.context.EndpointResponse;

import java.util.List;

/**
 */
public class GetPhase extends BasePersistencePhase<HyperionContext>
{
    @Override
    protected void executePhase(HyperionContext phaseContext) throws Exception
    {
        EntityPlugin plugin = phaseContext.getEntityPlugin();
        List ids = plugin.getKeyConverter().covertKeys(phaseContext.getId());

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
