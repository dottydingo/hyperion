package com.dottydingo.hyperion.core.persistence;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.HistoryEntry;
import com.dottydingo.hyperion.core.endpoint.EndpointSort;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.dao.support.ChainedPersistenceExceptionTranslator;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 */
public class TransactionalDecorator<C extends ApiObject<ID>, ID extends Serializable>
        implements PersistenceOperations<C, ID>, BeanFactoryAware
{
    private PersistenceOperations<C, ID> delegate;

    private TransactionTemplate readWriteTransactionTemplate;
    private TransactionTemplate readOnlyTransactionTemplate;

    private volatile PersistenceExceptionTranslator persistenceExceptionTranslator;


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        if (!(beanFactory instanceof ListableBeanFactory))
            throw new IllegalArgumentException(
                    "Cannot use PersistenceExceptionTranslator autodetection without ListableBeanFactory");
        persistenceExceptionTranslator = detectPersistenceExceptionTranslators((ListableBeanFactory) beanFactory);
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager)
    {
        if (transactionManager == null)
        {
            throw new RuntimeException("Transaction manager can not be null");
        }

        readWriteTransactionTemplate = new TransactionTemplate(transactionManager, getReadWriteTransaction());
        readOnlyTransactionTemplate = new TransactionTemplate(transactionManager, getReadOnlyTransaction());
    }

    public void setDelegate(PersistenceOperations<C, ID> delegate)
    {
        this.delegate = delegate;
    }

    @Override
    public List<C> findByIds(final List<ID> ids, final PersistenceContext context)
    {
        return readOnlyTransactionTemplate.execute(new MappingExceptionCallback<List<C>>()
        {
            @Override
            public List<C> doInTransactionInternal(TransactionStatus status)
            {
                return delegate.findByIds(ids, context);
            }
        });

    }

    @Override
    public QueryResult<C> query(final Node query, final Integer start, final Integer limit, final EndpointSort sort,
                                final PersistenceContext context)
    {
        return readOnlyTransactionTemplate.execute(new MappingExceptionCallback<QueryResult<C>>()
        {
            @Override
            public QueryResult<C> doInTransactionInternal(TransactionStatus status)
            {
                return delegate.query(query, start, limit, sort, context);
            }
        });

    }

    @Override
    public List<C> createOrUpdateItems(final List<C> clientItems, final PersistenceContext context)
    {
        return readWriteTransactionTemplate.execute(new MappingExceptionCallback<List<C>>()
        {
            @Override
            public List<C> doInTransactionInternal(TransactionStatus status)
            {
                return delegate.createOrUpdateItems(clientItems, context);
            }
        });
    }

    @Override
    public List<C> updateItems(final List<C> clientItems, final PersistenceContext context)
    {
        return readWriteTransactionTemplate.execute(new MappingExceptionCallback<List<C>>()
        {
            @Override
            public List<C> doInTransactionInternal(TransactionStatus status)
            {
                return delegate.updateItems(clientItems, context);
            }
        });
    }

    @Override
    public C updateItem(final ID id, final C item, final PersistenceContext context)
    {
        return readWriteTransactionTemplate.execute(new MappingExceptionCallback<C>()
        {
            @Override
            public C doInTransactionInternal(TransactionStatus status)
            {
                return delegate.updateItem(id, item, context);
            }
        });
    }

    @Override
    public int deleteItem(final List<ID> ids, final PersistenceContext context)
    {
        return readWriteTransactionTemplate.execute(new MappingExceptionCallback<Integer>()
        {
            @Override
            public Integer doInTransactionInternal(TransactionStatus status)
            {
                return delegate.deleteItem(ids, context);
            }
        });
    }

    @Override
    public QueryResult<HistoryEntry> getHistory(final ID id, final Integer start, final Integer limit,
                                                final PersistenceContext context)
    {
        return readOnlyTransactionTemplate.execute(new MappingExceptionCallback<QueryResult<HistoryEntry>>()
        {
            @Override
            public QueryResult<HistoryEntry> doInTransactionInternal(TransactionStatus status)
            {
                return delegate.getHistory(id, start, limit, context);
            }
        });
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

    protected PersistenceExceptionTranslator detectPersistenceExceptionTranslators(ListableBeanFactory beanFactory)
    {
        // adapted from org.springframework.dao.support.PersistenceExceptionTranslationInterceptor

        // Find all translators, being careful not to activate FactoryBeans.
        Map<String, PersistenceExceptionTranslator> pets = BeanFactoryUtils.beansOfTypeIncludingAncestors(
                beanFactory, PersistenceExceptionTranslator.class, false, false);
        ChainedPersistenceExceptionTranslator cpet = new ChainedPersistenceExceptionTranslator();
        for (PersistenceExceptionTranslator pet : pets.values())
        {
            cpet.addDelegate(pet);
        }
        return cpet;
    }

    private abstract class MappingExceptionCallback<T> implements TransactionCallback<T>
    {
        @Override
        public T doInTransaction(TransactionStatus status)
        {
            try
            {
                return doInTransactionInternal(status);
            }
            catch (RuntimeException e)
            {
                throw DataAccessUtils.translateIfNecessary(e, persistenceExceptionTranslator);
            }
        }

        abstract protected T doInTransactionInternal(TransactionStatus status);
    }
}
