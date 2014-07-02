package com.dottydingo.hyperion.core.persistence.history;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.core.endpoint.marshall.EndpointMarshaller;
import com.dottydingo.hyperion.core.model.PersistentHistoryEntry;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.hyperion.core.registry.ApiVersionPlugin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 */
public class HistorySerializer
{
    private final SimpleBeanFilter beanFilter = new SimpleBeanFilter();

    private EndpointMarshaller endpointMarshaller;

    public void setEndpointMarshaller(EndpointMarshaller endpointMarshaller)
    {
        this.endpointMarshaller = endpointMarshaller;
    }

    public <C extends ApiObject> String serializeHistoryEntry(C entry, PersistenceContext context)
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        endpointMarshaller.marshall(os,entry);
        return os.toString();
    }

    public <C extends ApiObject> C deserializeHistoryEntry(PersistentHistoryEntry entry,PersistenceContext context)
    {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(entry.getSerializedEntry().getBytes());
        ApiVersionPlugin savedVersion = context.getEntityPlugin().getApiVersionRegistry().getPluginForVersion(entry.getApiVersion());
        C apiEntry = (C) endpointMarshaller.unmarshall(inputStream,savedVersion.getApiClass());

        C copy = beanFilter.copy(apiEntry,context.getAuthorizationContext());
        return copy;
    }

}
