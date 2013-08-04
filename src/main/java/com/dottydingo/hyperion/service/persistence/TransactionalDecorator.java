package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.service.endpoint.HistoryEntry;
import com.dottydingo.hyperion.service.model.BasePersistentHistoryEntry;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.Serializable;
import java.util.List;

/**
 */
public class TransactionalDecorator<C extends ApiObject, ID extends Serializable> implements PersistenceOperations<C, ID>
{
    private PlatformTransactionManager transactionManager;
    private PersistenceOperations<C,ID> delegate;

    public void setTransactionManager(PlatformTransactionManager transactionManager)
    {
        this.transactionManager = transactionManager;
    }

    public void setDelegate(PersistenceOperations<C, ID> delegate)
    {
        this.delegate = delegate;
    }

    @Override
    public List<C> findByIds(List<ID> ids, PersistenceContext context)
    {
        TransactionStatus status = transactionManager.getTransaction(getReadOnlyTransaction());

        try
        {
            return delegate.findByIds(ids, context);
        }
        finally
        {
            transactionManager.commit(status);
        }
    }

    @Override
    public QueryResult<C> query(String query, Integer start, Integer limit, String sort, PersistenceContext context)
    {
        TransactionStatus status = transactionManager.getTransaction(getReadOnlyTransaction());
        try
        {
            return delegate.query(query, start, limit, sort, context);
        }
        finally
        {
            transactionManager.commit(status);
        }
    }

    @Override
    public C createOrUpdateItem(C item, PersistenceContext context)
    {
        TransactionStatus status = transactionManager.getTransaction(getReadWriteTransaction());
        try
        {
            return delegate.createOrUpdateItem(item, context);
        }
        catch (RuntimeException e)
        {
            status.setRollbackOnly();
            throw e;
        }
        finally
        {
            transactionManager.commit(status);
        }
    }

    @Override
    public C updateItem(List<ID> ids, C item, PersistenceContext context)
    {
        TransactionStatus status = transactionManager.getTransaction(getReadWriteTransaction());
        try
        {
            return delegate.updateItem(ids, item, context);
        }
        catch (RuntimeException e)
        {
            status.setRollbackOnly();
            throw e;
        }
        finally
        {
            transactionManager.commit(status);
        }
    }

    @Override
    public int deleteItem(List<ID> ids, PersistenceContext context)
    {
        TransactionStatus status = transactionManager.getTransaction(getReadWriteTransaction());
        try
        {
            return delegate.deleteItem(ids, context);
        }
        catch (RuntimeException e)
        {
            status.setRollbackOnly();
            throw e;
        }
        finally
        {
            transactionManager.commit(status);
        }
    }

    @Override
    public QueryResult<HistoryEntry> getHistory(ID id, Integer start, Integer limit, PersistenceContext context)
    {
        TransactionStatus status = transactionManager.getTransaction(getReadOnlyTransaction());
        try
        {
            return delegate.getHistory(id, start, limit, context);
        }
        finally
        {
            transactionManager.commit(status);
        }
    }

    protected TransactionDefinition getReadOnlyTransaction()
    {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setReadOnly(true);

        return transactionDefinition;
    }

    protected TransactionDefinition getReadWriteTransaction()
    {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setReadOnly(false);

        return transactionDefinition;
    }
}
