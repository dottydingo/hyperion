package com.dottydingo.hyperion.core.persistence;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.exception.ConflictException;
import com.dottydingo.hyperion.api.exception.HyperionException;
import com.dottydingo.hyperion.api.HistoryEntry;
import com.dottydingo.hyperion.core.endpoint.EndpointSort;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.dao.OptimisticLockingFailureException;

import java.io.Serializable;
import java.util.List;

/**
 */
public class ExceptionMappingDecorator<C extends ApiObject, ID extends Serializable> implements PersistenceOperations<C, ID>
{
    private static final String ITEM_CHANGED = "ERROR_ITEM_CHANGED";
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
        if(ex instanceof OptimisticLockingFailureException)
        {
            return new ConflictException(context.getMessageSource().getErrorMessage(ITEM_CHANGED,context.getLocale()));
        }
        return ex;
    }
}
