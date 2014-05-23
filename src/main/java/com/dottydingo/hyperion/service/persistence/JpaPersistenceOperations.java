package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.exception.NotFoundException;
import com.dottydingo.hyperion.exception.ValidationException;
import com.dottydingo.hyperion.service.configuration.ApiVersionPlugin;
import com.dottydingo.hyperion.api.HistoryAction;
import com.dottydingo.hyperion.api.HistoryEntry;
import com.dottydingo.hyperion.service.model.BasePersistentHistoryEntry;
import com.dottydingo.hyperion.service.model.PersistentObject;
import com.dottydingo.hyperion.service.persistence.dao.Dao;
import com.dottydingo.hyperion.service.persistence.dao.PersistentQueryResult;
import com.dottydingo.hyperion.service.persistence.query.PredicateBuilder;
import com.dottydingo.hyperion.service.persistence.query.PredicateBuilderFactory;
import com.dottydingo.hyperion.service.persistence.query.RsqlPredicateBuilderFactory;
import com.dottydingo.hyperion.service.persistence.sort.OrderBuilder;
import com.dottydingo.hyperion.service.persistence.sort.OrderBuilderFactory;
import com.dottydingo.hyperion.service.translation.Translator;

import java.io.Serializable;
import java.util.*;

/**
 */
public class JpaPersistenceOperations<C extends ApiObject, P extends PersistentObject<ID>, ID extends Serializable>
        implements PersistenceOperations<C,ID>
{

    protected PredicateBuilderFactory predicateBuilderFactory;
    protected OrderBuilderFactory<P> orderBuilderFactory;
    protected HistoryEntryFactory historyEntryFactory;

    public void setPredicateBuilderFactory(RsqlPredicateBuilderFactory predicateBuilderFactory)
    {
        this.predicateBuilderFactory = predicateBuilderFactory;
    }

    public void setOrderBuilderFactory(OrderBuilderFactory<P> orderBuilderFactory)
    {
        this.orderBuilderFactory = orderBuilderFactory;
    }

    public void setHistoryEntryFactory(HistoryEntryFactory historyEntryFactory)
    {
        this.historyEntryFactory = historyEntryFactory;
    }


    @Override
    public List<C> findByIds(List<ID> ids, PersistenceContext context)
    {

        ApiVersionPlugin<C,P> apiVersionPlugin = context.getApiVersionPlugin();


        List<P> iterable = context.getEntityPlugin().getDao().findAll(context.getEntityPlugin().getEntityClass(),ids);

        List<P> result = new ArrayList<P>();
        for (P p : iterable)
        {
            if(context.getEntityPlugin().getPersistenceFilter().isVisible(p,context))
                result.add(p);
        }

        return apiVersionPlugin.getTranslator().convertPersistent(result,context);
    }

    @Override
    public QueryResult<C> query(String query, Integer start, Integer limit, String sort, PersistenceContext context)
    {
        ApiVersionPlugin<C,P> apiVersionPlugin = context.getApiVersionPlugin();

        int size = limit;
        int pageStart = start == null ? 0 : start - 1;


        List<PredicateBuilder<P>> predicateBuilders = new ArrayList<PredicateBuilder<P>>();
        if(query != null && query.length() > 0)
        {
            predicateBuilders.add(predicateBuilderFactory.createPredicateBuilder(query,context.getEntityPlugin()));
        }

        PredicateBuilder<P> filter = context.getEntityPlugin().getPersistenceFilter().getFilterPredicateBuilder(context);
        if(filter != null)
            predicateBuilders.add(filter);

        OrderBuilder<P> orderBuilder = orderBuilderFactory.createOrderBuilder(sort, context.getEntityPlugin());

        Dao<P,ID> dao = context.getEntityPlugin().getDao();
        PersistentQueryResult<P> all = dao.query(context.getEntityPlugin().getEntityClass(), pageStart, size,
                orderBuilder, predicateBuilders);

        List<C> converted;
        if(all.getTotalCount() > 0)
        {
            List<P> list = all.getResults();
            converted = apiVersionPlugin.getTranslator().convertPersistent(list,context);
        }
        else
            converted = Collections.emptyList();

        QueryResult<C> queryResult= new QueryResult<C>();
        queryResult.setItems(converted);
        queryResult.setResponseCount(converted.size());
        queryResult.setTotalCount(all.getTotalCount());
        queryResult.setStart(start == null ? 1 : (start));

        return queryResult;
    }

    @Override
    public C createOrUpdateItem(C item, PersistenceContext context)
    {
        CreateKeyProcessor<C,ID> createKeyProcessor = context.getEntityPlugin().getCreateKeyProcessor();
        if(createKeyProcessor != null)
        {
            ID id = createKeyProcessor.lookup(item);
            if(id != null)
                return updateItem(Collections.singletonList(id),item,context);
        }

        ApiVersionPlugin<C,P> apiVersionPlugin = context.getApiVersionPlugin();

        apiVersionPlugin.getValidator().validateCreate(item, context);

        Dao<P,ID> dao = context.getEntityPlugin().getDao();

        context.setCurrentTimestamp(dao.getCurrentTimestamp());

        Translator<C,P> translator = apiVersionPlugin.getTranslator();
        P persistent = translator.convertClient(item, context);

        if(!context.getEntityPlugin().getPersistenceFilter().canCreate(persistent,context))
            return null;

        P saved = doCreate(persistent,context);

        if(context.getEntityPlugin().isHistoryEnabled())
            saveHistory(context,saved,HistoryAction.create);

        C toReturn = translator.convertPersistent(saved,context);
        context.setWriteContext(WriteContext.create);

        EntityChangeListener entityChangeListener = context.getEntityPlugin().getEntityChangeListener();
        if(entityChangeListener != null)
        {
            EntityChangeEvent<C> entityChangeEvent = new EntityChangeEvent<C>(context.getEntity(), null,toReturn,null,
                    context);
            context.addEntityChangeEvent(entityChangeEvent);
        }

        return toReturn;
    }

    protected P doCreate(P persistent,PersistenceContext context)
    {
        Dao<P,ID> dao = context.getEntityPlugin().getDao();
        return dao.create(persistent);
    }


    @Override
    public C updateItem(List<ID> ids, C item, PersistenceContext context)
    {
        ApiVersionPlugin<C,P> apiVersionPlugin = context.getApiVersionPlugin();

        Translator<C,P> translator = apiVersionPlugin.getTranslator();

        Dao<P,ID> dao = context.getEntityPlugin().getDao();
        P existing = dao.find(context.getEntityPlugin().getEntityClass(),ids.get(0));

        if(existing == null)
            throw new NotFoundException(
                    String.format("%s with id %s was not found.",context.getEntity(),ids.get(0)));

        apiVersionPlugin.getValidator().validateUpdate(item,existing, context);

        if(!context.getEntityPlugin().getPersistenceFilter().canUpdate(existing,context))
        {
            return null;
        }

        EntityChangeListener entityChangeListener = context.getEntityPlugin().getEntityChangeListener();

        // only capture the original if there is a listener
        C originalItem = null;
        if(entityChangeListener != null)
        {
            originalItem = translator.convertPersistent(existing,context);
        }
        context.setCurrentTimestamp(dao.getCurrentTimestamp());

        // todo this needs a better implementation...
        ID oldId = existing.getId();

        boolean dirty = translator.copyClient(item, existing,context);

        if(oldId != null && !oldId.equals(existing.getId()))
            throw new ValidationException("Id in URI does not match the Id in the payload.");

        context.setWriteContext(WriteContext.update);
        if(dirty)
        {
            P persistent = doUpdate(context, existing);
            if(context.getEntityPlugin().isHistoryEnabled())
                saveHistory(context,persistent,HistoryAction.modify);

            C toReturn = translator.convertPersistent(persistent, context);

            if(entityChangeListener != null)
            {
                EntityChangeEvent<C> entityChangeEvent = new EntityChangeEvent<C>(context.getEntity(), originalItem,
                        toReturn, context.getChangedFields(), context);
                context.addEntityChangeEvent(entityChangeEvent);
            }

            return toReturn;
        }
        else
        {
            // make sure we don't persist anything unintentionally
            existing = dao.reset(existing);
            return translator.convertPersistent(existing, context);
        }

    }

    protected P doUpdate(PersistenceContext context, P existing)
    {
        Dao<P,ID> dao = context.getEntityPlugin().getDao();
        return dao.update(existing);
    }

    @Override
    public int deleteItem(List<ID> ids, PersistenceContext context)
    {

        Dao<P,ID> dao = context.getEntityPlugin().getDao();
        ApiVersionPlugin<C,P> apiVersionPlugin = context.getApiVersionPlugin();
        EntityChangeListener entityChangeListener = context.getEntityPlugin().getEntityChangeListener();
        Translator<C,P> translator = apiVersionPlugin.getTranslator();
        Iterable<P> persistentItems = dao.findAll(context.getEntityPlugin().getEntityClass(),ids);
        int deleted = 0;
        for (P item : persistentItems)
        {
            if(context.getEntityPlugin().getPersistenceFilter().canDelete(item,context))
            {
                apiVersionPlugin.getValidator().validateDelete(item, context);
                doDelete(context, item);
                if(context.getEntityPlugin().isHistoryEnabled())
                    saveHistory(context,item,HistoryAction.delete);

                if(entityChangeListener != null)
                {
                    C originalItem = translator.convertPersistent(item, context);
                    EntityChangeEvent<C> entityChangeEvent = new EntityChangeEvent<C>(context.getEntity(), originalItem,
                            null, null, context);
                    context.addEntityChangeEvent(entityChangeEvent);
                }
                deleted++;
            }
        }

        return deleted;
    }

    protected void doDelete(PersistenceContext context, P item)
    {
        Dao<P,ID> dao = context.getEntityPlugin().getDao();
        dao.delete(item);
    }


    @Override
    public QueryResult<HistoryEntry> getHistory(ID id, Integer start, Integer limit, PersistenceContext context)
    {
        QueryResult<HistoryEntry> result = new QueryResult<HistoryEntry>();
        Dao<P,ID> dao = context.getEntityPlugin().getDao();
        PersistentQueryResult<BasePersistentHistoryEntry> history = dao.getHistory(context.getEntityPlugin().getHistoryType(),
                context.getEntity(), id, start, limit);

        result.setTotalCount(history.getTotalCount());
        result.setStart(start == null ? 1 : start);
        if(history.getResults() != null)
        {
            List<HistoryEntry> historyEntries = new LinkedList<HistoryEntry>();
            result.setItems(historyEntries);
            for (BasePersistentHistoryEntry entry : history.getResults())
            {
                HistoryEntry historyEntry = new HistoryEntry();
                historyEntry.setId(entry.getEntityId());
                historyEntry.setHistoryAction(entry.getHistoryAction());
                historyEntry.setTimestamp(entry.getTimestamp());
                historyEntry.setUser(entry.getUser());
                historyEntry.setApiVersion(entry.getApiVersion());
                historyEntry.setEntry(historyEntryFactory.readEntry(entry,context));
                historyEntries.add(historyEntry);
            }
        }
        else
            result.setItems(Collections.EMPTY_LIST);

        result.setResponseCount(result.getItems().size());

        return result;
    }

    protected void saveHistory(PersistenceContext context, P entity,HistoryAction historyAction)
    {
        BasePersistentHistoryEntry entry = historyEntryFactory.generateHistory(context,entity,historyAction);
        Dao<P,ID> dao = context.getEntityPlugin().getDao();
        dao.saveHistory(entry);
    }
}
