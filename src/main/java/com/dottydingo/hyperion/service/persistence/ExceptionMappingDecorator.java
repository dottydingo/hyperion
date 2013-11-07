package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.exception.HyperionException;
import com.dottydingo.hyperion.api.HistoryEntry;
import org.springframework.dao.OptimisticLockingFailureException;

import java.io.Serializable;
import java.util.List;

/**
 */
public class ExceptionMappingDecorator<C extends ApiObject, ID extends Serializable> implements PersistenceOperations<C, ID>
{
    private PersistenceOperations<C,ID> delegate;

    public void setDelegate(PersistenceOperations<C, ID> delegate)
    {
        try
        {
            this.delegate = delegate;
        }
        catch (RuntimeException e)
        {
            throw mapException(e);
        }
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
            throw mapException(e);
        }
    }

    @Override
    public QueryResult<C> query(String query, Integer start, Integer limit, String sort, PersistenceContext context)
    {
        try
        {
            return delegate.query(query, start, limit, sort, context);
        }
        catch (RuntimeException e)
        {
            throw mapException(e);
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
            throw mapException(e);
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
            throw mapException(e);
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
            throw mapException(e);
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
            throw mapException(e);
        }
    }

    protected RuntimeException mapException(RuntimeException ex)
    {
        if(ex instanceof OptimisticLockingFailureException)
        {
            return new HyperionException(409,"Item has been changed.");
        }
        return ex;
    }
}
