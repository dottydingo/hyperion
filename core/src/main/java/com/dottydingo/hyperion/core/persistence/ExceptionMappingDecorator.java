package com.dottydingo.hyperion.core.persistence;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.exception.ConflictException;
import com.dottydingo.hyperion.api.exception.HyperionException;
import com.dottydingo.hyperion.api.HistoryEntry;
import com.dottydingo.hyperion.api.exception.ServiceUnavailableException;
import com.dottydingo.hyperion.core.endpoint.EndpointSort;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.dao.*;

import java.io.Serializable;
import java.util.List;

/**
 */
public class ExceptionMappingDecorator<C extends ApiObject, ID extends Serializable> implements PersistenceOperations<C, ID>
{
    private static final String ITEM_CHANGED = "ERROR_ITEM_CHANGED";
    private static final String UNCAUGHT_CONFLICT = "ERROR_UNCAUGHT_CONFLICT";
    private static final String QUERY_TIMEOUT = "ERROR_QUERY_TIMEOUT";
    private static final String DATA_ACCESS_FAILURE = "ERROR_DATA_ACCESS_FAILURE";

    private PersistenceOperations<C,ID> delegate;

    public void setDelegate(PersistenceOperations<C, ID> delegate)
    {
        this.delegate = delegate;
    }

    @Override
    public List<C> findByIds(List<ID> ids, PersistenceContext context)
    {
        try
        {
            return delegate.findByIds(ids, context);
        }
        catch (RuntimeException e)
        {
            throw mapException(e, context);
        }
    }

    @Override
    public QueryResult<C> query(Node query, Integer start, Integer limit, EndpointSort sort, PersistenceContext context)
    {
        try
        {
            return delegate.query(query, start, limit, sort, context);
        }
        catch (RuntimeException e)
        {
            throw mapException(e, context);
        }
    }

    @Override
    public C createOrUpdateItem(C item, PersistenceContext context)
    {
        try
        {
            return delegate.createOrUpdateItem(item, context);
        }
        catch (RuntimeException e)
        {
            throw mapException(e, context);
        }
    }

    @Override
    public C updateItem(List<ID> ids, C item, PersistenceContext context)
    {
        try
        {
            return delegate.updateItem(ids, item, context);
        }
        catch (RuntimeException e)
        {
            throw mapException(e, context);
        }
    }

    @Override
    public int deleteItem(List<ID> ids, PersistenceContext context)
    {
        try
        {
            return delegate.deleteItem(ids, context);
        }
        catch (RuntimeException e)
        {
            throw mapException(e, context);
        }
    }

    @Override
    public QueryResult<HistoryEntry> getHistory(ID id, Integer start, Integer limit, PersistenceContext context)
    {
        try
        {
            return delegate.getHistory(id, start, limit, context);
        }
        catch (RuntimeException e)
        {
            throw mapException(e, context);
        }
    }

    protected RuntimeException mapException(RuntimeException ex, PersistenceContext context)
    {
        RuntimeException convertedException = convertException(ex, context);
        if(convertedException != null)
            return convertedException;

        return ex;
    }

    protected RuntimeException convertException(RuntimeException ex, PersistenceContext context)
    {
        if(ex instanceof ConcurrencyFailureException)
        {
            return new ConflictException(context.getMessageSource().getErrorMessage(ITEM_CHANGED,context.getLocale()));
        }
        else if(ex instanceof QueryTimeoutException)
        {
            return new ServiceUnavailableException(context.getMessageSource().getErrorMessage(QUERY_TIMEOUT,context.getLocale()));
        }
        else if(ex instanceof DataIntegrityViolationException)
        {
            return new ConflictException(context.getMessageSource().getErrorMessage(UNCAUGHT_CONFLICT,
                    context.getLocale(),ex.getMessage()));
        }
        else if(ex instanceof NonTransientDataAccessResourceException)
        {
            return new ServiceUnavailableException(context.getMessageSource().getErrorMessage(DATA_ACCESS_FAILURE,
                    context.getLocale()));
        }
        return null;
    }
}
