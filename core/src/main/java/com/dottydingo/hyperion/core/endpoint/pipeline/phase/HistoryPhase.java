package com.dottydingo.hyperion.core.endpoint.pipeline.phase;

import com.dottydingo.hyperion.api.Page;
import com.dottydingo.hyperion.api.exception.BadRequestException;
import com.dottydingo.hyperion.api.v1.LegacyHistoryResponse;
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
 * Perform a history operation
 */
public class HistoryPhase extends BasePersistencePhase
{

    @Override
    protected void executePhase(HyperionContext phaseContext) throws Exception
    {
        EndpointRequest request = phaseContext.getEndpointRequest();
        EndpointResponse response = phaseContext.getEndpointResponse();

        EntityPlugin plugin = phaseContext.getEntityPlugin();
        List<Serializable> ids = convertIds(phaseContext, plugin);
        if(ids.size() != 1)
            throw new BadRequestException(messageSource.getErrorMessage(ERROR_SINGLE_ID_REQUIRED,phaseContext.getLocale()));

        Integer start = getIntegerParameter("start",phaseContext);
        Integer limit = getIntegerParameter("limit",phaseContext);

        if(start != null && start < 1)
            throw new BadRequestException(messageSource.getErrorMessage(BAD_START_PARAMETER,phaseContext.getLocale()));

        if(limit != null && limit < 1)
            throw new BadRequestException(messageSource.getErrorMessage(BAD_LIMIT_PARAMETER,phaseContext.getLocale()));

        PersistenceContext persistenceContext = buildPersistenceContext(phaseContext);

        PersistenceOperations operations = persistenceContext.getEntityPlugin().getPersistenceOperations();
        QueryResult<HistoryEntry> entries = operations.getHistory(ids.get(0),start,limit,persistenceContext);

        if(phaseContext.isLegacyClient())
        {
            LegacyHistoryResponse historyResponse = new LegacyHistoryResponse();
            historyResponse.setEntries(entries.getItems());
            historyResponse.setTotalCount(entries.getTotalCount());
            historyResponse.setStart(entries.getStart());
            historyResponse.setResponseCount(entries.getResponseCount());

            phaseContext.setResult(historyResponse);
        }
        else
        {
            HistoryResponse historyResponse = new HistoryResponse();
            historyResponse.setEntries(entries.getItems());

            Page page = new Page();
            page.setTotalCount(entries.getTotalCount());
            page.setStart(entries.getStart());
            page.setResponseCount(entries.getResponseCount());

            historyResponse.setPage(page);

            phaseContext.setResult(historyResponse);
        }

        response.setResponseCode(200);
    }
}
