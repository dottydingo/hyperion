package com.dottydingo.hyperion.core.persistence;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.exception.NotFoundException;
import com.dottydingo.hyperion.api.exception.ValidationException;
import com.dottydingo.hyperion.core.persistence.event.*;
import com.dottydingo.hyperion.core.persistence.history.HistorySerializer;
import com.dottydingo.hyperion.core.registry.ApiVersionPlugin;
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
import com.dottydingo.hyperion.core.validation.Validator;
import cz.jirutka.rsql.parser.ast.Node;

import java.io.Serializable;
import java.util.*;

/**
 */
public class DefaultPersistenceOperations<C extends ApiObject, P extends PersistentObject<ID>, ID extends Serializable>
        implements PersistenceOperations<C,ID>
{

    private static final String ID_MISMATCH = "VALIDATION_ID_MISMATCH";
    private static final String ID_MISSING = "VALIDATION_ID_MISSING";
    private static final String IDS_NOT_FOUND = "ERROR_ITEMS_NOT_FOUND";
    private static final String ITEM_NOT_FOUND = "ERROR_ITEM_NOT_FOUND";

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
    public QueryResult<C> query(Node query, Integer start, Integer limit, EndpointSort sort, PersistenceContext context)
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
    public List<C> createOrUpdateItems(List<C> clientItems, PersistenceContext context)
    {

        ApiVersionPlugin<C,P> apiVersionPlugin = context.getApiVersionPlugin();
        CreateKeyProcessor<C,ID> createKeyProcessor = apiVersionPlugin.getCreateKeyProcessor();
        Validator<C, P> validator = apiVersionPlugin.getValidator();
        EntityPlugin entityPlugin = context.getEntityPlugin();
        Dao<P,ID,?,?> dao = entityPlugin.getDao();
        Translator<C,P> translator = apiVersionPlugin.getTranslator();

        context.setCurrentTimestamp(dao.getCurrentTimestamp());


        AdminPersistenceContext adminPersistenceContext = null;

        if(entityPlugin.hasListeners())
            adminPersistenceContext = new AdminPersistenceContext(context);

        List<C> toReturn = new ArrayList<>(clientItems.size());

        for (C item : clientItems)
        {
            if(createKeyProcessor != null)
            {
                ID id = createKeyProcessor.lookup(item,context);
                if(id != null)
                {
                    toReturn.add(updateItem(id, item, context));
                    continue;
                }
            }

            validator.validateCreate(item, context);

            P persistent = translator.convertClient(item, context);

            P saved = doCreate(persistent, context);

            // reload the item from the db
            saved = (P) dao.find(entityPlugin.getEntityClass(), saved.getId());
            toReturn.add(translator.convertPersistent(saved, context));

            if(entityPlugin.hasListeners())
            {

                C newObject = translator.convertPersistent(saved,adminPersistenceContext);
                PersistentChangeEvent<C, ID> entityChangeEvent =
                        new PersistentChangeEvent<C, ID>(null, newObject, null,
                                context, saved.getId(), EntityChangeAction.CREATE);

                if(entityPlugin.hasPersistentChangeListeners())
                {
                    processPersistentChangeEvents(context, Collections.singletonList(entityChangeEvent));
                }

                if(entityPlugin.hasEntityChangeListeners())
                    context.addEntityChangeEvent(entityChangeEvent);
            }
        }

        return toReturn;
    }

    protected P doCreate(P persistent,PersistenceContext context)
    {
        Dao<P,ID,?,?> dao = context.getEntityPlugin().getDao();
        return dao.create(persistent);
    }

    @Override
    public List<C> updateItems(List<C> clientItems, PersistenceContext context)
    {
        ApiVersionPlugin<C,P> apiVersionPlugin = context.getApiVersionPlugin();

        Translator<C,P> translator = apiVersionPlugin.getTranslator();

        EntityPlugin entityPlugin = context.getEntityPlugin();
        Dao<P,ID,?,?> dao = entityPlugin.getDao();

        Map<ID,C> mappedClients = new LinkedHashMap<>(clientItems.size());
        for (C item : clientItems)
        {
            ID id = translator.convertId(item,context);
            if(id == null)
                throw new ValidationException(
                        context.getMessageSource().getErrorMessage(ID_MISSING, context.getLocale(),
                                context.getEntity()));
            mappedClients.put(id, item);
        }

        List<P> existingPersistent = dao.findAll(entityPlugin.getEntityClass(), new ArrayList<>(mappedClients.keySet()));
        if(existingPersistent.size() != mappedClients.size())
        {
            throw new NotFoundException(
                    context.getMessageSource().getErrorMessage(IDS_NOT_FOUND, context.getLocale(),
                            context.getEntity(),findMissing(new ArrayList<>(mappedClients.keySet()), existingPersistent)));
        }

        AdminPersistenceContext adminPersistenceContext = null;
        if(entityPlugin.hasListeners())
            adminPersistenceContext = new AdminPersistenceContext(context);

        context.setCurrentTimestamp(dao.getCurrentTimestamp());

        List<C> toReturn = new LinkedList<>();
        Map<ID,P> mappedPersistentItems = buildMap(existingPersistent);
        for (Map.Entry<ID, C> entry : mappedClients.entrySet())
        {
            P existing = mappedPersistentItems.get(entry.getKey());
            C item = entry.getValue();

            P updated = updateItem(context, adminPersistenceContext, existing, item);

            toReturn.add(translator.convertPersistent(updated,context));
        }

        return toReturn;

    }

    protected P updateItem(PersistenceContext context, AdminPersistenceContext adminPersistenceContext, P existing, C item)
    {
        ApiVersionPlugin<C, P> apiVersionPlugin = context.getApiVersionPlugin();
        Translator<C, P> translator = apiVersionPlugin.getTranslator();
        EntityPlugin entityPlugin = context.getEntityPlugin();
        Dao<P, ID, ?, ?> dao = entityPlugin.getDao();

        if(!entityPlugin.getPersistenceFilter().canUpdate(existing,context))
            return existing; // todo revisit this behavior

        apiVersionPlugin.getValidator().validateUpdate(item,existing, context);

        // only capture the original if there is a listener
        C originalItem = null;
        if(entityPlugin.hasListeners())
        {
            originalItem = translator.convertPersistent(existing,adminPersistenceContext);
        }

        boolean dirty = translator.copyClient(item, existing,context);
        if(dirty)
        {
            P persistent = doUpdate(context, existing);

            // refresh the saved item
            persistent = dao.find(entityPlugin.getEntityClass(), persistent.getId());

            if(entityPlugin.hasListeners())
            {
                C updatedItem = translator.convertPersistent(persistent,adminPersistenceContext);
                PersistentChangeEvent<C,ID> entityChangeEvent = new PersistentChangeEvent<>(originalItem,
                        updatedItem, context.getChangedFields(context.getEntity(),persistent.getId()),
                        context,persistent.getId(), EntityChangeAction.MODIFY);

                if(entityPlugin.hasPersistentChangeListeners())
                    processPersistentChangeEvents(context, Collections.singletonList(entityChangeEvent));

                if(entityPlugin.hasEntityChangeListeners())
                    context.addEntityChangeEvent(entityChangeEvent);

            }
            return persistent;
        }
        else
            return dao.reset(existing);
    }

    @Override
    public C updateItem(ID id, C item, PersistenceContext context)
    {
        ApiVersionPlugin<C,P> apiVersionPlugin = context.getApiVersionPlugin();

        Translator<C,P> translator = apiVersionPlugin.getTranslator();

        if(item.getId() != null)
        {
            if(!id.equals(translator.convertId(item,context)))
            throw new ValidationException(
                    context.getMessageSource().getValidationMessage(ID_MISMATCH,context.getLocale()));

        }

        EntityPlugin entityPlugin = context.getEntityPlugin();
        Dao<P,ID,?,?> dao = entityPlugin.getDao();
        P existing = (P)dao.find(entityPlugin.getEntityClass(),id);

        if(existing == null)
            throw new NotFoundException(
                    context.getMessageSource().getErrorMessage(ITEM_NOT_FOUND, context.getLocale(),
                            context.getEntity(), id));

        AdminPersistenceContext adminPersistenceContext = null;
        if(entityPlugin.hasListeners())
            adminPersistenceContext = new AdminPersistenceContext(context);

        P updated = updateItem(context,adminPersistenceContext,existing,item);

        return translator.convertPersistent(updated, context);

    }

    protected Map<ID,P> buildMap(List<P> found)
    {
        Map<ID,P> map = new HashMap<>();
        for (P p : found)
        {
            map.put(p.getId(),p);
        }

        return map;
    }

    protected List<ID> findMissing(List<ID> ids,List<P> found)
    {
        Set<ID> notFound = new HashSet<>(ids);
        for (P p : found)
        {
            notFound.remove(p.getId());
        }

        return new ArrayList<>(notFound);
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
        List<P> persistentItems = dao.findAll(entityPlugin.getEntityClass(),ids);
        PersistenceFilter persistenceFilter = entityPlugin.getPersistenceFilter();

        AdminPersistenceContext adminPersistenceContext = null;
        if(entityPlugin.hasListeners())
        {
            context.setCurrentTimestamp(dao.getCurrentTimestamp());
            adminPersistenceContext = new AdminPersistenceContext(context);
        }

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
            processPersistentChangeEvents(context, deleteEvents);

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

    protected void processPersistentChangeEvents(PersistenceContext context, List<PersistentChangeEvent<C, ID>> changeEvents)
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
