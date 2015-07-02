package com.dottydingo.hyperion.core.endpoint.pipeline.phase;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.EntityList;
import com.dottydingo.hyperion.api.EntityResponse;
import com.dottydingo.hyperion.api.exception.BadRequestException;
import com.dottydingo.hyperion.core.endpoint.HyperionContext;
import com.dottydingo.hyperion.core.endpoint.marshall.EndpointMarshaller;
import com.dottydingo.hyperion.core.endpoint.marshall.MarshallingException;
import com.dottydingo.hyperion.core.model.PersistentObject;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.hyperion.core.registry.ApiVersionPlugin;
import com.dottydingo.hyperion.core.registry.EntityPlugin;
import com.dottydingo.service.endpoint.context.EndpointRequest;
import com.dottydingo.service.endpoint.context.EndpointResponse;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 */
public class CreatePhase extends BasePersistencePhase
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
            processCollection(phaseContext);
        else
            processSingle(phaseContext);

    }

    private void processSingle(HyperionContext phaseContext)
    {
        EndpointRequest request = phaseContext.getEndpointRequest();
        EndpointResponse response = phaseContext.getEndpointResponse();

        ApiVersionPlugin<ApiObject,PersistentObject> apiVersionPlugin = phaseContext.getVersionPlugin();
        EntityPlugin plugin = phaseContext.getEntityPlugin();

        ApiObject clientObject = null;
        try
        {
            clientObject = marshaller.unmarshall(request.getInputStream(),apiVersionPlugin.getApiClass());
        }
        catch (MarshallingException e)
        {
            throw new BadRequestException(messageSource.getErrorMessage(ERROR_READING_REQUEST,phaseContext.getLocale(),e.getMessage()),e);
        }
        clientObject.setId(null);

        PersistenceContext persistenceContext = buildPersistenceContext(phaseContext);
        Set<String> fieldSet = persistenceContext.getRequestedFields();
        if(fieldSet != null)
            fieldSet.add("id");

        ApiObject saved = (ApiObject) plugin.getPersistenceOperations().createOrUpdateItems(
                Collections.singletonList(clientObject),
                persistenceContext).get(0);

        processChangeEvents(phaseContext, persistenceContext);
        response.setResponseCode(200);
        phaseContext.setResult(saved);
    }

    private void processCollection(HyperionContext phaseContext)
    {
        EndpointRequest request = phaseContext.getEndpointRequest();

        ApiVersionPlugin<ApiObject,PersistentObject> apiVersionPlugin = phaseContext.getVersionPlugin();
        EntityPlugin plugin = phaseContext.getEntityPlugin();

        List<ApiObject> clientObjects = null;
        try
        {
            clientObjects = marshaller.unmarshallCollection(request.getInputStream(), apiVersionPlugin.getApiClass());
        }
        catch (MarshallingException e)
        {
            throw new BadRequestException(messageSource.getErrorMessage(ERROR_READING_REQUEST,phaseContext.getLocale(),e.getMessage()),e);
        }


        PersistenceContext persistenceContext = buildPersistenceContext(phaseContext);
        Set<String> fieldSet = persistenceContext.getRequestedFields();
        if(fieldSet != null)
            fieldSet.add("id");

        List<ApiObject> saved =  plugin.getPersistenceOperations().createOrUpdateItems(clientObjects, persistenceContext);

        processChangeEvents(phaseContext,persistenceContext);

        EntityList<ApiObject> entityResponse = new EntityList<>();
        entityResponse.setEntries(saved);
        phaseContext.setResult(entityResponse);

    }
}
