package com.dottydingo.hyperion.core.endpoint.pipeline.phase;

import com.dottydingo.hyperion.api.exception.BadRequestException;
import com.dottydingo.hyperion.core.configuration.HyperionEndpointConfiguration;
import com.dottydingo.hyperion.api.EntityResponse;
import com.dottydingo.hyperion.core.endpoint.EndpointSort;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.hyperion.core.persistence.QueryResult;
import com.dottydingo.hyperion.core.endpoint.HyperionContext;
import com.dottydingo.service.endpoint.context.EndpointRequest;
import com.dottydingo.service.endpoint.context.EndpointResponse;
import cz.jirutka.rsql.parser.ParseException;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.RSQLParserException;
import cz.jirutka.rsql.parser.ast.Node;

/**
 */
public class QueryPhase extends BasePersistencePhase<HyperionContext>
{
    private HyperionEndpointConfiguration configuration;
    private EndpointSortBuilder endpointSortBuilder;

    public void setConfiguration(HyperionEndpointConfiguration configuration)
    {
        this.configuration = configuration;
    }

    public void setEndpointSortBuilder(EndpointSortBuilder endpointSortBuilder)
    {
        this.endpointSortBuilder = endpointSortBuilder;
    }

    @Override
    protected void executePhase(HyperionContext phaseContext) throws Exception
    {
        EndpointRequest request = phaseContext.getEndpointRequest();
        EndpointResponse response = phaseContext.getEndpointResponse();

        Integer start = getIntegerParameter("start",request);
        Integer limit = getIntegerParameter("limit",request);

        String query = request.getFirstParameter("query");
        String sort = request.getFirstParameter("sort");

        if(start != null && start < 1)
            throw new BadRequestException("The start parameter must be greater than zero.");

        if(limit != null && limit < 1)
            throw new BadRequestException("The limit parameter must be greater than zero.");

        if(limit == null)
            limit = configuration.getDefaultLimit();

        if(limit > configuration.getMaxLimit())
            throw new BadRequestException(String.format("The limit parameter can not be greater than %d.",configuration.getMaxLimit()));

        PersistenceContext persistenceContext = buildPersistenceContext(phaseContext);

        EndpointSort requestedSorts = endpointSortBuilder.buildSort(sort, persistenceContext);
        Node queryExpression = null;

        if(query != null && query.trim().length() > 0)
            queryExpression = buildQueryExpression(query, persistenceContext);

        QueryResult queryResult = phaseContext.getEntityPlugin()
                .getPersistenceOperations()
                .query(queryExpression, start, limit, requestedSorts, persistenceContext);

        EntityResponse entityResponse = new EntityResponse();
        entityResponse.setEntries(queryResult.getItems());
        entityResponse.setResponseCount(queryResult.getResponseCount());
        entityResponse.setStart(queryResult.getStart());
        entityResponse.setTotalCount(queryResult.getTotalCount());

        phaseContext.setResult(entityResponse);

        response.setResponseCode(200);


    }

    protected Node buildQueryExpression(String query,PersistenceContext persistenceContext)
    {
        try
        {
            logger.debug("Parsing query: {}", query);
            return new RSQLParser().parse(query);
        }
        catch (RSQLParserException ex)
        {
            throw new BadRequestException("Error parsing query.", ex);
        }
    }
}
