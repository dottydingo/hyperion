package com.dottydingo.hyperion.core.persistence;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.exception.ConflictException;
import com.dottydingo.hyperion.api.exception.HyperionException;
import com.dottydingo.hyperion.api.HistoryEntry;
import com.dottydingo.hyperion.api.exception.InternalException;
import com.dottydingo.hyperion.api.exception.ServiceUnavailableException;
import com.dottydingo.hyperion.core.endpoint.EndpointSort;
import com.mchange.v2.resourcepool.ResourcePoolException;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.dao.*;
import org.springframework.transaction.TransactionException;

import java.io.Serializable;
import java.util.List;

/**
 */
public class ExceptionMappingDecorator<C extends ApiObject<ID>, ID extends Serializable> implements PersistenceOperations<C, ID>
{
    private static final String ITEM_CHANGED = "ERROR_ITEM_CHANGED";
    private static final String UNCAUGHT_CONFLICT = "ERROR_UNCAUGHT_CONFLICT";
    private static final String QUERY_TIMEOUT = "ERROR_QUERY_TIMEOUT";
    private static final String DATA_ACCESS_FAILURE = "ERROR_DATA_ACCESS_FAILURE";

    private static boolean c3poDetected = detectC3p0();

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
        catch (HyperionException e)
        {
            throw e;
        }
        catch (Exception e)
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
        catch (HyperionException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw mapException(e, context);
        }
    }

    @Override
    public List<C> createOrUpdateItems(List<C> clientItems, PersistenceContext context)
    {
        try
        {
            return delegate.createOrUpdateItems(clientItems, context);
        }
        catch (HyperionException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw mapException(e, context);
        }
    }

    @Override
    public List<C> updateItems(List<C> clientItems, PersistenceContext context)
    {
        try
        {
            return delegate.updateItems(clientItems, context);
        }
        catch (HyperionException e)
        {
            throw e;
        }
        catch (Exception e)
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
        catch (HyperionException e)
        {
            throw e;
        }
        catch (Exception e)
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
        catch (HyperionException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw mapException(e, context);
        }
    }

    protected RuntimeException mapException(Exception ex, PersistenceContext context)
    {
        RuntimeException convertedException = convertException(ex, context);
        if(convertedException != null)
            return convertedException;

        return new RuntimeException(ex);
    }

    protected RuntimeException convertException(Exception ex, PersistenceContext context)
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
                    context.getLocale(),getCause(ex).getMessage(),ex));

        }
        else if(ex instanceof NonTransientDataAccessResourceException)
        {
            return new ServiceUnavailableException(context.getMessageSource().getErrorMessage(DATA_ACCESS_FAILURE,
                    context.getLocale(),ex));
        }else if(ex instanceof TransactionException)
        {
            return new ServiceUnavailableException(context.getMessageSource().getErrorMessage(DATA_ACCESS_FAILURE,
                    context.getLocale(),ex));
        }
        if(c3poDetected)
        {
            return convertC3p0Exception(ex, context);
        }

        return null;
    }

    protected RuntimeException convertC3p0Exception(Exception ex, PersistenceContext context)
    {
        if(ex instanceof ResourcePoolException)
            return new ServiceUnavailableException(context.getMessageSource().getErrorMessage(DATA_ACCESS_FAILURE,
                    context.getLocale()));

        return null;
    }

    private static boolean detectC3p0()
    {
        try
        {
            ExceptionMappingDecorator.class.getClassLoader().loadClass("com.mchange.v2.c3p0.ComboPooledDataSource");
            return true;

        }
        catch (ClassNotFoundException e)
        {
            return false;
        }

    }

    private Throwable getCause(Throwable t)
    {
        Throwable cause = t;
        while (cause.getCause() != null)
        {
            cause = cause.getCause();
        }

        return cause;
    }
}
