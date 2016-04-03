package com.dottydingo.hyperion.core.endpoint.pipeline.phase;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.EntityList;
import com.dottydingo.hyperion.api.exception.BadRequestException;
import com.dottydingo.hyperion.core.endpoint.marshall.MarshallingException;
import com.dottydingo.hyperion.core.endpoint.marshall.WriteLimitException;
import com.dottydingo.hyperion.core.registry.ApiVersionPlugin;
import com.dottydingo.hyperion.core.registry.EntityPlugin;
import com.dottydingo.hyperion.core.endpoint.marshall.EndpointMarshaller;
import com.dottydingo.hyperion.core.endpoint.marshall.RequestContext;
import com.dottydingo.hyperion.core.model.PersistentObject;
import com.dottydingo.hyperion.core.endpoint.HyperionContext;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.service.endpoint.context.EndpointRequest;
import com.dottydingo.service.endpoint.context.EndpointResponse;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Perform an update (PUT) operation
 */
public class UpdatePhase extends BasePersistencePhase
{
    private EndpointMarshaller marshaller;

    public void setMarshaller(EndpointMarshaller marshaller)
    {
        this.marshaller = marshaller;
    }

    @Override
    protected void executePhase(HyperionContext hyperionContext) throws Exception
    {
        if(hyperionContext.isLegacyClient())
            processLegacyRequest(hyperionContext);
        else
            processCollectionRequest(hyperionContext);
    }

    /**
     * Process a legacy V1 request (single item)
     * @param hyperionContext The context
     */
    protected void processLegacyRequest(HyperionContext hyperionContext)
    {
        EndpointRequest request = hyperionContext.getEndpointRequest();
        EndpointResponse response = hyperionContext.getEndpointResponse();

        ApiVersionPlugin<ApiObject<Serializable>,PersistentObject<Serializable>,Serializable> apiVersionPlugin = hyperionContext.getVersionPlugin();
        EntityPlugin plugin = hyperionContext.getEntityPlugin();

        PersistenceContext persistenceContext = buildPersistenceContext(hyperionContext);

        Set<String> fieldSet = persistenceContext.getRequestedFields();
        if(fieldSet != null)
            fieldSet.add("id");

        RequestContext<ApiObject<Serializable>> requestContext = null;
        try
        {
            requestContext = marshaller.unmarshallWithContext(request.getInputStream(), apiVersionPlugin.getApiClass());
        }
        catch (MarshallingException e)
        {
            throw new BadRequestException(messageSource.getErrorMessage(ERROR_READING_REQUEST, hyperionContext.getLocale(),
                    e.getMessage()),e);
        }
        ApiObject clientObject = requestContext.getRequestObject();

        List<Serializable> ids = convertIds(hyperionContext, plugin);
        if(ids.size() != 1)
            throw new BadRequestException(messageSource.getErrorMessage(ERROR_SINGLE_ID_REQUIRED,hyperionContext.getLocale()));

        persistenceContext.setProvidedFields(requestContext.getProvidedFields());

        ApiObject saved = (ApiObject) plugin.getPersistenceOperations().updateItems(
                Collections.singletonList(clientObject),
                persistenceContext).get(0);

        processChangeEvents(hyperionContext,persistenceContext);

        response.setResponseCode(200);
        hyperionContext.setResult(saved);
    }

    /**
     * Process a multi-item request
     * @param hyperionContext The context
     */
    protected void processCollectionRequest(HyperionContext hyperionContext)
    {
        EndpointRequest request = hyperionContext.getEndpointRequest();

        ApiVersionPlugin<ApiObject<Serializable>,PersistentObject<Serializable>,Serializable> apiVersionPlugin = hyperionContext.getVersionPlugin();
        EntityPlugin plugin = hyperionContext.getEntityPlugin();

        PersistenceContext persistenceContext = buildPersistenceContext(hyperionContext);

        Set<String> fieldSet = persistenceContext.getRequestedFields();
        if(fieldSet != null)
            fieldSet.add("id");

        RequestContext<List<ApiObject<Serializable>>> requestContext = null;
        try
        {
            requestContext = marshaller.unmarshallCollectionWithContext(request.getInputStream(),
                    apiVersionPlugin.getApiClass());
        }
        catch (WriteLimitException e)
        {
            throw new BadRequestException(messageSource.getErrorMessage(ERROR_WRITE_LIMIT, hyperionContext.getLocale(),e.getWriteLimit()),e);
        }
        catch (MarshallingException e)
        {
            throw new BadRequestException(messageSource.getErrorMessage(ERROR_READING_REQUEST, hyperionContext.getLocale(),
                    e.getMessage()),e);
        }
        List<ApiObject<Serializable>> clientObjects = requestContext.getRequestObject();

        persistenceContext.setProvidedFields(requestContext.getProvidedFields());

        List<ApiObject> saved = plugin.getPersistenceOperations().updateItems(clientObjects, persistenceContext);

        processChangeEvents(hyperionContext,persistenceContext);

        EntityList<ApiObject> entityResponse = new EntityList<>();
        entityResponse.setEntries(saved);
        hyperionContext.setResult(entityResponse);
    }

}
