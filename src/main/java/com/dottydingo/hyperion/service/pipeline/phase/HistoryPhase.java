package com.dottydingo.hyperion.service.pipeline.phase;

import com.dottydingo.hyperion.exception.BadRequestException;
import com.dottydingo.hyperion.service.configuration.EntityPlugin;
import com.dottydingo.hyperion.service.context.HyperionContext;
import com.dottydingo.hyperion.service.endpoint.HistoryEntry;
import com.dottydingo.hyperion.service.endpoint.HistoryResponse;
import com.dottydingo.hyperion.service.model.BasePersistentHistoryEntry;
import com.dottydingo.hyperion.service.persistence.HistoryEntryFactory;
import com.dottydingo.hyperion.service.persistence.PersistenceContext;
import com.dottydingo.hyperion.service.persistence.PersistenceOperations;
import com.dottydingo.service.endpoint.context.EndpointRequest;
import com.dottydingo.service.endpoint.context.EndpointResponse;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 */
public class HistoryPhase extends BasePersistencePhase<HyperionContext>
{
    private HistoryEntryFactory historyEntryFactory;

    public void setHistoryEntryFactory(HistoryEntryFactory historyEntryFactory)
    {
        this.historyEntryFactory = historyEntryFactory;
    }

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
        List<BasePersistentHistoryEntry> entries = operations.getHistory(ids.get(0),start,limit,persistenceContext);

        HistoryResponse historyResponse = new HistoryResponse();
        List<HistoryEntry> historyEntries = new LinkedList<HistoryEntry>();
        historyResponse.setEntries(historyEntries);

        for (BasePersistentHistoryEntry entry : entries)
        {
            HistoryEntry historyEntry = new HistoryEntry();
            historyEntry.setId(entry.getEntityId());
            historyEntry.setHistoryAction(entry.getHistoryAction());
            historyEntry.setTimestamp(entry.getTimestamp());
            historyEntry.setUser(entry.getUser());
            historyEntry.setEntry(historyEntryFactory.readEntry(entry,persistenceContext));
            historyEntries.add(historyEntry);
        }

        phaseContext.setResult(historyResponse);

        response.setResponseCode(200);
    }
}
