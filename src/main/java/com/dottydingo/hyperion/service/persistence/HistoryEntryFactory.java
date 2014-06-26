package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.exception.InternalException;
import com.dottydingo.hyperion.service.configuration.ApiVersionPlugin;
import com.dottydingo.hyperion.api.HistoryAction;
import com.dottydingo.hyperion.service.marshall.EndpointMarshaller;
import com.dottydingo.hyperion.service.model.BasePersistentHistoryEntry;
import com.dottydingo.hyperion.service.model.PersistentObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 */
public class HistoryEntryFactory
{
    private final SimpleBeanFilter beanFilter = new SimpleBeanFilter();

    private EndpointMarshaller endpointMarshaller;

    public void setEndpointMarshaller(EndpointMarshaller endpointMarshaller)
    {
        this.endpointMarshaller = endpointMarshaller;
    }

    public <H extends BasePersistentHistoryEntry,P extends PersistentObject,C extends ApiObject>  H generateHistory(PersistenceContext context,
                                                                                                                    P entity,
                                                                                                                    HistoryAction historyAction)
    {
        try
        {
            H entry = (H) context.getEntityPlugin().getHistoryType().newInstance();

            entry.setEntityType(context.getEntity());
            entry.setEntityId(entity.getId());

            ApiVersionPlugin<C,P> apiVersionPlugin = context.getApiVersionPlugin();
            if(apiVersionPlugin == null)
                apiVersionPlugin = context.getEntityPlugin().getApiVersionRegistry().getPluginForVersion(null);

            entry.setApiVersion(apiVersionPlugin.getVersion());
            entry.setHistoryAction(historyAction);
            entry.setUser(context.getUserContext().getUserId());
            entry.setTimestamp(context.getCurrentTimestamp());

            C apiInstance = apiVersionPlugin.getTranslator().convertPersistent(entity,context);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            endpointMarshaller.marshall(os,apiInstance);
            entry.setSerializedEntry(os.toString());

            return entry;
        }

        catch (Exception e)
        {
            throw new InternalException("Error processing history entry.",e);
        }
    }

    public <C extends ApiObject> C readEntry(BasePersistentHistoryEntry entry,PersistenceContext context)
    {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(entry.getSerializedEntry().getBytes());
        ApiVersionPlugin savedVersion = context.getEntityPlugin().getApiVersionRegistry().getPluginForVersion(entry.getApiVersion());
        C apiEntry = (C) endpointMarshaller.unmarshall(inputStream,savedVersion.getApiClass());

        C copy = beanFilter.copy(apiEntry,context.getAuthorizationContext());
        return copy;
    }

}
