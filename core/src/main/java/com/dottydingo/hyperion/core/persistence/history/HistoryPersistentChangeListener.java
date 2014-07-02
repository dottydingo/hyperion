package com.dottydingo.hyperion.core.persistence.history;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.HistoryAction;
import com.dottydingo.hyperion.api.exception.InternalException;
import com.dottydingo.hyperion.core.model.PersistentHistoryEntry;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.hyperion.core.persistence.event.*;
import com.dottydingo.hyperion.core.registry.ApiVersionPlugin;

import java.io.Serializable;

/**
 */
// todo: localize
public class HistoryPersistentChangeListener implements PersistentChangeListener<ApiObject,Serializable>
{
    private HistorySerializer historySerializer;

    public void setHistorySerializer(HistorySerializer historySerializer)
    {
        this.historySerializer = historySerializer;
    }

    @Override
    public void processEntityChange(PersistentChangeEvent<ApiObject,Serializable> event)
    {
        PersistenceContext context = event.getPersistenceContext();
        if(!context.getEntityPlugin().isHistoryEnabled())
            return;

        try
        {
            PersistentHistoryEntry entry = generateHistoryEntry(event, context);
            context.getEntityPlugin().getDao().saveHistory(entry);
        }
        catch (Exception e)
        {
            throw new InternalException("Error processing history entry.", e);
        }
    }

    protected PersistentHistoryEntry generateHistoryEntry(PersistentChangeEvent<ApiObject, Serializable> event,
                                                          PersistenceContext context)
            throws Exception

    {
        PersistentHistoryEntry entry =
                (PersistentHistoryEntry) context.getEntityPlugin().getHistoryType().newInstance();

        entry.setEntityType(context.getEntity());
        entry.setEntityId(event.getId());

        ApiVersionPlugin apiVersionPlugin = context.getApiVersionPlugin();

        entry.setApiVersion(apiVersionPlugin.getVersion());
        entry.setHistoryAction(getAction(event.getEntityChangeAction()));
        entry.setUser(context.getUserContext().getUserId());
        entry.setTimestamp(context.getCurrentTimestamp());


        ApiObject entity = entry.getHistoryAction() == HistoryAction.DELETE
                ? event.getOriginalItem()
                : event.getUpdatedItem();

        entry.setSerializedEntry(historySerializer.serializeHistoryEntry(entity,context));
        return entry;
    }

    protected HistoryAction getAction(EntityChangeAction entityChangeAction)
    {
        switch (entityChangeAction)
        {
            case CREATE:
                return HistoryAction.CREATE;
            case MODIFY:
                return HistoryAction.MODIFY;
            case DELETE:
                return HistoryAction.DELETE;
            default:
                throw new InternalException(String.format("Unknown entity change action: %s",entityChangeAction));
        }

    }
}
