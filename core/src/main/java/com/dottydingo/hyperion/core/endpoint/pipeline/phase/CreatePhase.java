package com.dottydingo.hyperion.core.endpoint.pipeline.phase;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.exception.BadRequestException;
import com.dottydingo.hyperion.core.endpoint.marshall.MarshallingException;
import com.dottydingo.hyperion.core.registry.ApiVersionPlugin;
import com.dottydingo.hyperion.core.registry.EntityPlugin;
import com.dottydingo.hyperion.core.endpoint.marshall.EndpointMarshaller;
import com.dottydingo.hyperion.core.model.PersistentObject;
import com.dottydingo.hyperion.core.endpoint.HyperionContext;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.service.endpoint.context.EndpointRequest;
import com.dottydingo.service.endpoint.context.EndpointResponse;

import java.util.Collections;
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

        ApiObject saved = (ApiObject) plugin.getPersistenceOperations().createOrUpdateItems(Collections.singletonList(clientObject),
                persistenceContext).get(0);
        if(saved != null)
        {
            processChangeEvents(phaseContext,persistenceContext);
            response.setResponseCode(200);
            phaseContext.setResult(saved);
        }
        else
            response.setResponseCode(304);

    }
}
