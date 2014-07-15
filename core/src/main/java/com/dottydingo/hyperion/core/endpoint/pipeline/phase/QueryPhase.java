package com.dottydingo.hyperion.core.endpoint.pipeline.phase;

import com.dottydingo.hyperion.api.exception.BadRequestException;
import com.dottydingo.hyperion.api.EntityResponse;
import com.dottydingo.hyperion.core.endpoint.EndpointSort;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.hyperion.core.persistence.QueryResult;
import com.dottydingo.hyperion.core.endpoint.HyperionContext;
import com.dottydingo.service.endpoint.context.EndpointRequest;
import com.dottydingo.service.endpoint.context.EndpointResponse;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.RSQLParserException;
import cz.jirutka.rsql.parser.ast.Node;

/**
 */
public class QueryPhase extends BasePersistencePhase
{
    private static final String INVALID_QUERY_STRING = "ERROR_INVALID_QUERY_STRING";
    private EndpointSortBuilder endpointSortBuilder;

    public void setEndpointSortBuilder(EndpointSortBuilder endpointSortBuilder)
    {
        this.endpointSortBuilder = endpointSortBuilder;
    }

    @Override
    protected void executePhase(HyperionContext phaseContext) throws Exception
    {
        EndpointRequest request = phaseContext.getEndpointRequest();
        EndpointResponse response = phaseContext.getEndpointResponse();

        Integer start = getIntegerParameter("start",phaseContext);
        Integer limit = getIntegerParameter("limit",phaseContext);

        String query = request.getFirstParameter("query");
        String sort = request.getFirstParameter("sort");

        if(start != null && start < 1)
            throw new BadRequestException(messageSource.getErrorMessage(BAD_START_PARAMETER,phaseContext.getLocale()));

        if(limit != null && limit < 1)
            throw new BadRequestException(messageSource.getErrorMessage(BAD_LIMIT_PARAMETER,phaseContext.getLocale()));

        if(limit == null)
            limit = configuration.getDefaultLimit();

        if(limit > configuration.getMaxLimit())
            throw new BadRequestException(messageSource.getErrorMessage("ERROR_MAX_LIMIT_EXCEEDED",
                    phaseContext.getLocale(),configuration.getMaxLimit()));

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
            throw new BadRequestException(persistenceContext.getMessageSource()
                    .getErrorMessage(INVALID_QUERY_STRING,persistenceContext.getLocale(),query));
        }
    }
}
