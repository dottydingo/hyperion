package com.dottydingo.hyperion.core.endpoint.pipeline.phase;

import com.dottydingo.hyperion.api.EntityList;
import com.dottydingo.hyperion.api.v1.LegacyEntityResponse;
import com.dottydingo.hyperion.core.registry.EntityPlugin;
import com.dottydingo.hyperion.api.EntityResponse;
import com.dottydingo.hyperion.core.endpoint.HyperionContext;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.service.endpoint.context.EndpointResponse;

import java.util.List;

/**
 * Perform a find operation (GET by IDs)
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

        if(phaseContext.isLegacyClient())
        {
            LegacyEntityResponse entityResponse = new LegacyEntityResponse();
            entityResponse.setEntries(converted);
            entityResponse.setResponseCount(converted.size());
            entityResponse.setStart(1);
            entityResponse.setTotalCount(new Long(converted.size()));
            phaseContext.setResult(entityResponse);
        }
        else
        {
            EntityList entityList = new EntityList();
            entityList.setEntries(converted);
            phaseContext.setResult(entityList);
        }

        EndpointResponse response = phaseContext.getEndpointResponse();
        response.setResponseCode(200);
    }
}
