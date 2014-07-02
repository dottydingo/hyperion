package com.dottydingo.hyperion.core.persistence;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.exception.NotFoundException;
import com.dottydingo.hyperion.api.exception.ValidationException;
import com.dottydingo.hyperion.core.persistence.event.*;
import com.dottydingo.hyperion.core.persistence.history.HistorySerializer;
import com.dottydingo.hyperion.core.registry.ApiVersionPlugin;
import com.dottydingo.hyperion.api.HistoryAction;
import com.dottydingo.hyperion.api.HistoryEntry;
import com.dottydingo.hyperion.core.endpoint.EndpointSort;
import com.dottydingo.hyperion.core.model.PersistentHistoryEntry;
import com.dottydingo.hyperion.core.model.PersistentObject;
import com.dottydingo.hyperion.core.persistence.dao.Dao;
import com.dottydingo.hyperion.core.persistence.dao.PersistentQueryResult;
import com.dottydingo.hyperion.core.persistence.query.PersistentQueryBuilder;
import com.dottydingo.hyperion.core.persistence.query.PersistentQueryBuilderFactory;
import com.dottydingo.hyperion.core.persistence.sort.PersistentOrderBuilder;
import com.dottydingo.hyperion.core.persistence.sort.PersistentOrderBuilderFactory;
import com.dottydingo.hyperion.core.registry.EntityPlugin;
import com.dottydingo.hyperion.core.translation.Translator;
import cz.jirutka.rsql.parser.model.Expression;

import java.io.Serializable;
import java.util.*;

/**
 */
public class DefaultPersistenceOperations<C extends ApiObject, P extends PersistentObject<ID>, ID extends Serializable>
        implements PersistenceOperations<C,ID>
{

    protected PersistentQueryBuilderFactory persistentQueryBuilderFactory;
    protected PersistentOrderBuilderFactory persistentOrderBuilderFactory;
    protected HistorySerializer historySerializer;

    public void setPersistentQueryBuilderFactory(PersistentQueryBuilderFactory persistentQueryBuilderFactory)
    {
        this.persistentQueryBuilderFactory = persistentQueryBuilderFactory;
    }

    public void setPersistentOrderBuilderFactory(PersistentOrderBuilderFactory persistentOrderBuilderFactory)
    {
        this.persistentOrderBuilderFactory = persistentOrderBuilderFactory;
    }

    public void setHistorySerializer(HistorySerializer historySerializer)
    {
        this.historySerializer = historySerializer;
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
    public QueryResult<C> query(Expression query, Integer start, Integer limit, EndpointSort sort, PersistenceContext context)
    {
        ApiVersionPlugin<C,P> apiVersionPlugin = context.getApiVersionPlugin();

        int size = limit;
        int pageStart = start == null ? 0 : start - 1;


        List<PersistentQueryBuilder> queryBuilders = new ArrayList<PersistentQueryBuilder>();
        if(query != null)
        {
            queryBuilders.add(
                    persistentQueryBuilderFactory.createQueryBuilder(query, context));
        }

        PersistentQueryBuilder filter = context.getEntityPlugin().getPersistenceFilter().getFilterQueryBuilder(
                context);
        if(filter != null)
            queryBuilders.add(filter);

        PersistentOrderBuilder<P> orderBuilder = persistentOrderBuilderFactory.createOrderBuilder(sort, context);

        Dao dao = context.getEntityPlugin().getDao();
        PersistentQueryResult<P> all = dao.query(context.getEntityPlugin().getEntityClass(), pageStart, size,
                orderBuilder, queryBuilders);

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

        ApiVersionPlugin<C,P> apiVersionPlugin = context.getApiVersionPlugin();
        CreateKeyProcessor<C,ID> createKeyProcessor = apiVersionPlugin.getCreateKeyProcessor();
        if(createKeyProcessor != null)
        {
            ID id = createKeyProcessor.lookup(item,context);
            if(id != null)
                return updateItem(Collections.singletonList(id),item,context);
        }

        apiVersionPlugin.getValidator().validateCreate(item, context);

        EntityPlugin entityPlugin = context.getEntityPlugin();
        Dao dao = entityPlugin.getDao();

        context.setCurrentTimestamp(dao.getCurrentTimestamp());

        Translator<C,P> translator = apiVersionPlugin.getTranslator();
        P persistent = translator.convertClient(item, context);

        if(!entityPlugin.getPersistenceFilter().canCreate(persistent,context))
            return null;

        P saved = doCreate(persistent,context);


        C toReturn = translator.convertPersistent(saved,context);
        context.setWriteContext(WriteContext.create);


        if(entityPlugin.hasListeners())
        {
            AdminPersistenceContext adminPersistenceContext = new AdminPersistenceContext(context);
            C newObject = translator.convertPersistent(saved,adminPersistenceContext);
            PersistentChangeEvent<C, ID> entityChangeEvent =
                    new PersistentChangeEvent<C, ID>(null, newObject, null,
                            context, saved.getId(), EntityChangeAction.CREATE);

            if(entityPlugin.hasEntityChangeListeners())
            {
                processEntityChangeEvents(context,Collections.singletonList(entityChangeEvent));
            }

            if(entityPlugin.hasEntityChangeListeners())
                context.addEntityChangeEvent(entityChangeEvent);

        }

        return toReturn;
    }



    protected P doCreate(P persistent,PersistenceContext context)
    {
        Dao<P,ID,?,?> dao = context.getEntityPlugin().getDao();
        return dao.create(persistent);
    }


    @Override
    public C updateItem(List<ID> ids, C item, PersistenceContext context)
    {
        ApiVersionPlugin<C,P> apiVersionPlugin = context.getApiVersionPlugin();

        Translator<C,P> translator = apiVersionPlugin.getTranslator();

        EntityPlugin entityPlugin = context.getEntityPlugin();
        Dao<P,ID,?,?> dao = entityPlugin.getDao();
        P existing = dao.find(entityPlugin.getEntityClass(),ids.get(0));

        if(existing == null)
            throw new NotFoundException(
                    String.format("%s with id %s was not found.",context.getEntity(),ids.get(0)));

        apiVersionPlugin.getValidator().validateUpdate(item,existing, context);

        if(!entityPlugin.getPersistenceFilter().canUpdate(existing,context))
        {
            return null;
        }

        AdminPersistenceContext adminPersistenceContext = null;
        if(entityPlugin.hasListeners())
            adminPersistenceContext = new AdminPersistenceContext(context);

        // only capture the original if there is a listener
        C originalItem = null;
        if(entityPlugin.hasListeners())
        {
            originalItem = translator.convertPersistent(existing,adminPersistenceContext);
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

            C toReturn = translator.convertPersistent(persistent, context);

            if(entityPlugin.hasListeners())
            {
                C updatedItem = translator.convertPersistent(persistent,adminPersistenceContext);
                PersistentChangeEvent<C,ID> entityChangeEvent = new PersistentChangeEvent<>(originalItem,
                        updatedItem, context.getChangedFields(), context,persistent.getId(),EntityChangeAction.MODIFY);

                if(entityPlugin.hasPersistentChangeListeners())
                    processEntityChangeEvents(context,Collections.singletonList(entityChangeEvent));

                if(entityPlugin.hasEntityChangeListeners())
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
        Dao<P,ID,?,?> dao = context.getEntityPlugin().getDao();
        return dao.update(existing);
    }

    @Override
    public int deleteItem(List<ID> ids, PersistenceContext context)
    {

        EntityPlugin entityPlugin = context.getEntityPlugin();
        Dao<P,ID,?,?> dao = entityPlugin.getDao();
        ApiVersionPlugin<C,P> apiVersionPlugin = context.getApiVersionPlugin();

        Translator<C,P> translator = apiVersionPlugin.getTranslator();
        Iterable<P> persistentItems = dao.findAll(entityPlugin.getEntityClass(),ids);
        PersistenceFilter persistenceFilter = entityPlugin.getPersistenceFilter();

        int deleted = 0;
        List<PersistentChangeEvent<C,ID>> deleteEvents = new ArrayList<>();
        for (P item : persistentItems)
        {

            if(persistenceFilter.canDelete(item, context))
            {
                apiVersionPlugin.getValidator().validateDelete(item, context);
                doDelete(context, item);

                if(entityPlugin.hasListeners())
                {
                    AdminPersistenceContext adminPersistenceContext = new AdminPersistenceContext(context);
                    C originalItem = translator.convertPersistent(item, adminPersistenceContext);
                    PersistentChangeEvent<C,ID> entityChangeEvent = new PersistentChangeEvent<>( originalItem,
                            null, null, context,item.getId(),EntityChangeAction.DELETE);

                    if(entityPlugin.hasPersistentChangeListeners())
                        deleteEvents.add(entityChangeEvent);

                    if(entityPlugin.hasEntityChangeListeners())
                        context.addEntityChangeEvent(entityChangeEvent);
                }
                deleted++;
            }
        }

        if(!deleteEvents.isEmpty())
            processEntityChangeEvents(context,deleteEvents);

        return deleted;
    }

    protected void doDelete(PersistenceContext context, P item)
    {
        Dao dao = context.getEntityPlugin().getDao();
        dao.delete(item);
    }


    @Override
    public QueryResult<HistoryEntry> getHistory(ID id, Integer start, Integer limit, PersistenceContext context)
    {
        QueryResult<HistoryEntry> result = new QueryResult<HistoryEntry>();
        Dao dao = context.getEntityPlugin().getDao();
        PersistentQueryResult<PersistentHistoryEntry> history = dao.getHistory(context.getEntityPlugin().getHistoryType(),
                context.getEntity(), id, start, limit);

        result.setTotalCount(history.getTotalCount());
        result.setStart(start == null ? 1 : start);
        if(history.getResults() != null)
        {
            List<HistoryEntry> historyEntries = new LinkedList<HistoryEntry>();
            result.setItems(historyEntries);
            for (PersistentHistoryEntry entry : history.getResults())
            {
                HistoryEntry historyEntry = new HistoryEntry();
                historyEntry.setId(entry.getEntityId());
                historyEntry.setHistoryAction(entry.getHistoryAction());
                historyEntry.setTimestamp(entry.getTimestamp());
                historyEntry.setUser(entry.getUser());
                historyEntry.setApiVersion(entry.getApiVersion());
                historyEntry.setEntry(historySerializer.deserializeHistoryEntry(entry,context));
                historyEntries.add(historyEntry);
            }
        }
        else
            result.setItems(Collections.EMPTY_LIST);

        result.setResponseCount(result.getItems().size());

        return result;
    }

    protected void processEntityChangeEvents(PersistenceContext context, List<PersistentChangeEvent<C, ID>> changeEvents)
    {
        List<PersistentChangeListener> listeners = context.getEntityPlugin().getPersistentChangeListeners();
        for (PersistentChangeListener listener : listeners)
        {
            for (PersistentChangeEvent<C, ID> changeEvent : changeEvents)
            {
                listener.processEntityChange(changeEvent);
            }
        }
    }

}
