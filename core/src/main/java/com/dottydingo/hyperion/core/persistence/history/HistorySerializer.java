package com.dottydingo.hyperion.core.persistence.history;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.exception.BadRequestException;
import com.dottydingo.hyperion.api.exception.InternalException;
import com.dottydingo.hyperion.core.endpoint.marshall.EndpointMarshaller;
import com.dottydingo.hyperion.core.endpoint.marshall.MarshallingException;
import com.dottydingo.hyperion.core.model.PersistentHistoryEntry;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.hyperion.core.registry.ApiVersionPlugin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 */
public class HistorySerializer
{
    public static final String CREATING_HISTORY = "ERROR_CREATING_HISTORY";
    public static final String READING_HISTORY = "ERROR_READING_HISTORY";
    private final SimpleBeanFilter beanFilter = new SimpleBeanFilter();

    private EndpointMarshaller endpointMarshaller;

    public void setEndpointMarshaller(EndpointMarshaller endpointMarshaller)
    {
        this.endpointMarshaller = endpointMarshaller;
    }

    public <C extends ApiObject> String serializeHistoryEntry(C entry, PersistenceContext context)
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try
        {
            endpointMarshaller.marshall(os,entry);
        }
        catch (MarshallingException e)
        {
            throw new InternalException(context.getMessageSource().getErrorMessage(CREATING_HISTORY,
                    context.getLocale(),context.getEntity()));
        }
        return os.toString();
    }

    public <C extends ApiObject> C deserializeHistoryEntry(PersistentHistoryEntry entry,PersistenceContext context)
    {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(entry.getSerializedEntry().getBytes());
        ApiVersionPlugin savedVersion = context.getEntityPlugin().getApiVersionRegistry().getPluginForVersion(entry.getApiVersion());
        C apiEntry = null;
        try
        {
            apiEntry = (C) endpointMarshaller.unmarshall(inputStream,savedVersion.getApiClass());
        }
        catch (MarshallingException e)
        {
            throw new InternalException(context.getMessageSource().getErrorMessage(READING_HISTORY,
                    context.getLocale(), context.getEntity()));
        }

        C copy = beanFilter.copy(apiEntry,context.getAuthorizationContext());
        return copy;
    }

}
