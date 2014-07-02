package com.dottydingo.hyperion.core.persistence;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.exception.NotFoundException;
import com.dottydingo.hyperion.api.exception.ValidationException;
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
    protected HistoryEntryFactory historyEntryFactory;

    public void setPersistentQueryBuilderFactory(PersistentQueryBuilderFactory persistentQueryBuilderFactory)
    {
        this.persistentQueryBuilderFactory = persistentQueryBuilderFactory;
    }

    public void setPersistentOrderBuilderFactory(PersistentOrderBuilderFactory persistentOrderBuilderFactory)
    {
        this.persistentOrderBuilderFactory = persistentOrderBuilderFactory;
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

        Dao dao = context.getEntityPlugin().getDao();

        context.setCurrentTimestamp(dao.getCurrentTimestamp());

        Translator<C,P> translator = apiVersionPlugin.getTranslator();
        P persistent = translator.convertClient(item, context);

        if(!context.getEntityPlugin().getPersistenceFilter().canCreate(persistent,context))
            return null;

        P saved = doCreate(persistent,context);


        AdminPersistenceContext adminPersistenceContext = null;
        if(context.getEntityPlugin().hasEntityChangeListeners()  || context.getEntityPlugin().isHistoryEnabled())
            adminPersistenceContext = new AdminPersistenceContext(context);

        if(context.getEntityPlugin().isHistoryEnabled())
            saveHistory(adminPersistenceContext,saved,HistoryAction.CREATE);

        C toReturn = translator.convertPersistent(saved,context);
        context.setWriteContext(WriteContext.create);


        if(context.getEntityPlugin().hasEntityChangeListeners())
        {
            C newObject = translator.convertPersistent(saved,adminPersistenceContext);
            EntityChangeEvent<C> entityChangeEvent = new EntityChangeEvent<C>(context.getEntity(), null,newObject,null,
                    context);
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

        Dao<P,ID,?,?> dao = context.getEntityPlugin().getDao();
        P existing = dao.find(context.getEntityPlugin().getEntityClass(),ids.get(0));

        if(existing == null)
            throw new NotFoundException(
                    String.format("%s with id %s was not found.",context.getEntity(),ids.get(0)));

        apiVersionPlugin.getValidator().validateUpdate(item,existing, context);

        if(!context.getEntityPlugin().getPersistenceFilter().canUpdate(existing,context))
        {
            return null;
        }

        AdminPersistenceContext adminPersistenceContext = null;
        if(context.getEntityPlugin().hasEntityChangeListeners() || context.getEntityPlugin().isHistoryEnabled())
            adminPersistenceContext = new AdminPersistenceContext(context);

        // only capture the original if there is a listener
        C originalItem = null;
        if(context.getEntityPlugin().hasEntityChangeListeners())
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
            if(context.getEntityPlugin().isHistoryEnabled())
                saveHistory(adminPersistenceContext,persistent,HistoryAction.MODIFY);

            C toReturn = translator.convertPersistent(persistent, context);

            if(context.getEntityPlugin().hasEntityChangeListeners())
            {
                C updatedItem = translator.convertPersistent(persistent,adminPersistenceContext);
                EntityChangeEvent<C> entityChangeEvent = new EntityChangeEvent<C>(context.getEntity(), originalItem,
                        updatedItem, context.getChangedFields(), context);
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

        Dao<P,ID,?,?> dao = context.getEntityPlugin().getDao();
        ApiVersionPlugin<C,P> apiVersionPlugin = context.getApiVersionPlugin();

        Translator<C,P> translator = apiVersionPlugin.getTranslator();
        Iterable<P> persistentItems = dao.findAll(context.getEntityPlugin().getEntityClass(),ids);
        int deleted = 0;
        for (P item : persistentItems)
        {
            if(context.getEntityPlugin().getPersistenceFilter().canDelete(item,context))
            {
                apiVersionPlugin.getValidator().validateDelete(item, context);
                doDelete(context, item);

                AdminPersistenceContext adminPersistenceContext = null;
                if(context.getEntityPlugin().hasEntityChangeListeners()|| context.getEntityPlugin().isHistoryEnabled())
                    adminPersistenceContext = new AdminPersistenceContext(context);

                if(context.getEntityPlugin().isHistoryEnabled())
                    saveHistory(adminPersistenceContext,item,HistoryAction.DELETE);

                if(context.getEntityPlugin().hasEntityChangeListeners())
                {
                    C originalItem = translator.convertPersistent(item, adminPersistenceContext);
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
                historyEntry.setEntry(historyEntryFactory.readEntry(entry,context));
                historyEntries.add(historyEntry);
            }
        }
        else
            result.setItems(Collections.EMPTY_LIST);

        result.setResponseCount(result.getItems().size());

        return result;
    }

    protected void saveHistory(AdminPersistenceContext context, P entity,HistoryAction historyAction)
    {
        PersistentHistoryEntry entry = historyEntryFactory.generateHistory(context,entity,historyAction);
        Dao dao = context.getEntityPlugin().getDao();
        dao.saveHistory(entry);
    }
}
