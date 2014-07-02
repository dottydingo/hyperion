package com.dottydingo.hyperion.core.endpoint.pipeline.phase;

import com.dottydingo.hyperion.api.exception.BadRequestException;
import com.dottydingo.hyperion.core.registry.EntityPlugin;
import com.dottydingo.hyperion.core.endpoint.HyperionContext;
import com.dottydingo.hyperion.api.HistoryEntry;
import com.dottydingo.hyperion.api.HistoryResponse;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.hyperion.core.persistence.PersistenceOperations;
import com.dottydingo.hyperion.core.persistence.QueryResult;
import com.dottydingo.service.endpoint.context.EndpointRequest;
import com.dottydingo.service.endpoint.context.EndpointResponse;

import java.io.Serializable;
import java.util.List;

/**
 */
public class HistoryPhase extends BasePersistencePhase<HyperionContext>
{
    @Override
    protected void executePhase(HyperionContext phaseContext) throws Exception
    {
        EndpointRequest request = phaseContext.getEndpointRequest();
        EndpointResponse response = phaseContext.getEndpointResponse();

        EntityPlugin plugin = phaseContext.getEntityPlugin();
        List<Serializable> ids = plugin.getKeyConverter().covertKeys(phaseContext.getId());
        if(ids.size() != 1)
            throw new BadRequestException("A single ID must be provided.");

        Integer start = getIntegerParameter("start",request);
        Integer limit = getIntegerParameter("limit",request);

        if(start != null && start < 1)
            throw new BadRequestException("The start parameter must be greater than zero.");

        if(limit != null && limit < 1)
            throw new BadRequestException("The limit parameter must be greater than zero.");

        PersistenceContext persistenceContext = buildPersistenceContext(phaseContext);

        PersistenceOperations operations = persistenceContext.getEntityPlugin().getPersistenceOperations();
        QueryResult<HistoryEntry> entries = operations.getHistory(ids.get(0),start,limit,persistenceContext);

        HistoryResponse historyResponse = new HistoryResponse();
        historyResponse.setEntries(entries.getItems());
        historyResponse.setTotalCount(entries.getTotalCount());
        historyResponse.setStart(entries.getStart());
        historyResponse.setResponseCount(entries.getResponseCount());

        phaseContext.setResult(historyResponse);

        response.setResponseCode(200);
    }
}
