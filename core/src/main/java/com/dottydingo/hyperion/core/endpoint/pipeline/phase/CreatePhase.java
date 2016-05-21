package com.dottydingo.hyperion.core.endpoint.pipeline.phase;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.EntityList;
import com.dottydingo.hyperion.api.exception.BadRequestException;
import com.dottydingo.hyperion.core.endpoint.HyperionContext;
import com.dottydingo.hyperion.core.endpoint.marshall.EndpointMarshaller;
import com.dottydingo.hyperion.core.endpoint.marshall.MarshallingException;
import com.dottydingo.hyperion.core.endpoint.marshall.WriteLimitException;
import com.dottydingo.hyperion.core.model.PersistentObject;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.hyperion.core.registry.ApiVersionPlugin;
import com.dottydingo.hyperion.core.registry.EntityPlugin;
import com.dottydingo.service.endpoint.context.EndpointRequest;
import com.dottydingo.service.endpoint.context.EndpointResponse;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Perform a create (POST) operation
 */
public class CreatePhase extends BasePersistencePhase
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

        ApiObject clientObject = null;
        try
        {
            clientObject = marshaller.unmarshall(request.getInputStream(),apiVersionPlugin.getApiClass());
        }
        catch (MarshallingException e)
        {
            throw new BadRequestException(messageSource.getErrorMessage(ERROR_READING_REQUEST,hyperionContext.getLocale(),e.getMessage()),e);
        }
        clientObject.setId(null);

        PersistenceContext persistenceContext = buildPersistenceContext(hyperionContext);
        Set<String> fieldSet = persistenceContext.getRequestedFields();
        if(fieldSet != null)
            fieldSet.add("id");

        ApiObject saved = (ApiObject) plugin.getPersistenceOperations().createOrUpdateItems(
                Collections.singletonList(clientObject),
                persistenceContext).get(0);

        processChangeEvents(hyperionContext, persistenceContext);
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
        EndpointResponse response = hyperionContext.getEndpointResponse();

        ApiVersionPlugin<ApiObject<Serializable>,PersistentObject<Serializable>,Serializable> apiVersionPlugin = hyperionContext.getVersionPlugin();
        EntityPlugin plugin = hyperionContext.getEntityPlugin();

        List<ApiObject<Serializable>> clientObjects = null;
        try
        {
            clientObjects = marshaller.unmarshallCollection(request.getInputStream(), apiVersionPlugin.getApiClass());
        }
        catch (WriteLimitException e)
        {
            throw new BadRequestException(messageSource.getErrorMessage(ERROR_WRITE_LIMIT,hyperionContext.getLocale(),e.getWriteLimit()),e);
        }
        catch (MarshallingException e)
        {
            throw new BadRequestException(messageSource.getErrorMessage(ERROR_READING_REQUEST,hyperionContext.getLocale(),e.getMessage()),e);
        }


        PersistenceContext persistenceContext = buildPersistenceContext(hyperionContext);
        Set<String> fieldSet = persistenceContext.getRequestedFields();
        if(fieldSet != null)
            fieldSet.add("id");

        List<ApiObject> saved =  plugin.getPersistenceOperations().createOrUpdateItems(clientObjects, persistenceContext);

        processChangeEvents(hyperionContext,persistenceContext);

        response.setResponseCode(200);

        EntityList<ApiObject> entityResponse = new EntityList<>();
        entityResponse.setEntries(saved);
        hyperionContext.setResult(entityResponse);
    }
}
