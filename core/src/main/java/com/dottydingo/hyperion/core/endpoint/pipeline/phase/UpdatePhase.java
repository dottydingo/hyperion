package com.dottydingo.hyperion.core.endpoint.pipeline.phase;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.EntityList;
import com.dottydingo.hyperion.api.EntityResponse;
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
import java.util.List;
import java.util.Set;

/**
 */
public class UpdatePhase extends BasePersistencePhase
{
    private EndpointMarshaller marshaller;

    public void setMarshaller(EndpointMarshaller marshaller)
    {
        this.marshaller = marshaller;
    }

    @Override
    protected void executePhase(HyperionContext phaseContext) throws Exception
    {
        boolean isCollection = isCollection(phaseContext);
        if(isCollection)
            processCollection((phaseContext));
        else
            processSingleItem(phaseContext);
    }

    private void processSingleItem(HyperionContext phaseContext)
    {
        EndpointRequest request = phaseContext.getEndpointRequest();
        EndpointResponse response = phaseContext.getEndpointResponse();

        ApiVersionPlugin<ApiObject<Serializable>,PersistentObject<Serializable>,Serializable> apiVersionPlugin = phaseContext.getVersionPlugin();
        EntityPlugin plugin = phaseContext.getEntityPlugin();

        PersistenceContext persistenceContext = buildPersistenceContext(phaseContext);

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
            throw new BadRequestException(messageSource.getErrorMessage(ERROR_READING_REQUEST, phaseContext.getLocale(),
                    e.getMessage()),e);
        }
        ApiObject clientObject = requestContext.getRequestObject();

        List<Serializable> ids = convertIds(phaseContext, plugin);
        if(ids.size() != 1)
            throw new BadRequestException(messageSource.getErrorMessage(ERROR_SINGLE_ID_REQUIRED,phaseContext.getLocale()));

        persistenceContext.setProvidedFields(requestContext.getProvidedFields());


        ApiObject saved = plugin.getPersistenceOperations().updateItem(ids.get(0), clientObject, persistenceContext);

        processChangeEvents(phaseContext,persistenceContext);

        response.setResponseCode(200);
        phaseContext.setResult(saved);

    }

    private void processCollection(HyperionContext phaseContext)
    {
        EndpointRequest request = phaseContext.getEndpointRequest();

        ApiVersionPlugin<ApiObject<Serializable>,PersistentObject<Serializable>,Serializable> apiVersionPlugin = phaseContext.getVersionPlugin();
        EntityPlugin plugin = phaseContext.getEntityPlugin();

        List<Serializable> ids = convertIds(phaseContext, plugin);
        if(ids.size() > 0)
            throw new BadRequestException(messageSource.getErrorMessage(ERROR_SINGLE_ID_REQUIRED,phaseContext.getLocale()));

        PersistenceContext persistenceContext = buildPersistenceContext(phaseContext);

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
            throw new BadRequestException(messageSource.getErrorMessage(ERROR_WRITE_LIMIT,phaseContext.getLocale(),e.getWriteLimit()),e);
        }
        catch (MarshallingException e)
        {
            throw new BadRequestException(messageSource.getErrorMessage(ERROR_READING_REQUEST, phaseContext.getLocale(),
                    e.getMessage()),e);
        }
        List<ApiObject<Serializable>> clientObjects = requestContext.getRequestObject();

        persistenceContext.setProvidedFields(requestContext.getProvidedFields());

        List<ApiObject> saved = plugin.getPersistenceOperations().updateItems(clientObjects, persistenceContext);

        processChangeEvents(phaseContext,persistenceContext);

        EntityList<ApiObject> entityResponse = new EntityList<>();
        entityResponse.setEntries(saved);
        phaseContext.setResult(entityResponse);
    }
}
