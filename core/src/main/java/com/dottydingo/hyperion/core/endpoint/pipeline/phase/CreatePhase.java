package com.dottydingo.hyperion.core.endpoint.pipeline.phase;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.EntityList;
import com.dottydingo.hyperion.api.EntityResponse;
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

        ApiVersionPlugin<ApiObject<Serializable>,PersistentObject<Serializable>,Serializable> apiVersionPlugin = phaseContext.getVersionPlugin();
        EntityPlugin plugin = phaseContext.getEntityPlugin();

        List<ApiObject<Serializable>> clientObjects = null;
        try
        {
            clientObjects = marshaller.unmarshallCollection(request.getInputStream(), apiVersionPlugin.getApiClass());
        }
        catch (WriteLimitException e)
        {
            throw new BadRequestException(messageSource.getErrorMessage(ERROR_WRITE_LIMIT,phaseContext.getLocale(),e.getWriteLimit()),e);
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
