package com.dottydingo.hyperion.service.endpoint;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.exception.BadRequestException;
import com.dottydingo.hyperion.exception.NotFoundException;
import com.dottydingo.hyperion.service.context.RequestContext;
import com.dottydingo.hyperion.service.context.RequestContextBuilder;
import com.dottydingo.hyperion.service.model.PersistentObject;
import com.dottydingo.hyperion.service.persistence.PersistenceOperations;
import com.dottydingo.hyperion.service.persistence.QueryResult;
import com.dottydingo.hyperion.service.configuration.ApiVersionPlugin;
import com.dottydingo.hyperion.service.configuration.EntityPlugin;
import com.dottydingo.hyperion.service.configuration.ServiceRegistry;
import com.dottydingo.hyperion.service.translation.Translator;
import org.springframework.transaction.annotation.Transactional;


import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.Set;

/**
 */
public abstract class AbstractDataServiceEndpoint<C extends ApiObject,P extends PersistentObject,ID extends Serializable>
{
    private ServiceRegistry serviceRegistry;
    private RequestContextBuilder requestContextBuilder;
    @Context
    private UriInfo uriInfo;

    public void setServiceRegistry(ServiceRegistry serviceRegistry)
    {
        this.serviceRegistry = serviceRegistry;
    }

    public void setRequestContextBuilder(RequestContextBuilder requestContextBuilder)
    {
        this.requestContextBuilder = requestContextBuilder;
    }

    @GET()
    @Transactional(readOnly = true)
    @Produces(MediaType.APPLICATION_JSON)
    public Response queryData(@PathParam("entity") String entity,
                              @QueryParam("fields") String fields,
                              @QueryParam("start")  Integer start,
                              @QueryParam("limit")  Integer limit,
                              @QueryParam("query") String query,
                              @QueryParam("sort") String sort,
                              @QueryParam("version")  Integer version)
    {
        EntityPlugin<C,P,ID> plugin = getEntityPlugin(entity);
        ApiVersionPlugin<C,P> apiVersionPlugin = plugin.getApiVersionRegistry().getPluginForVersion(version);

        RequestContext requestContext = requestContextBuilder.buildRequestContext(uriInfo,entity,fields);
        QueryResult<P> queryResult = plugin.getPersistenceOperations().query(query, start, limit, sort, requestContext);
        List<P> persistent = queryResult.getItems();

        List<C> converted = apiVersionPlugin.getTranslator().convertPersistent(persistent,requestContext);

        EntityResponse<C> entityResponse = new EntityResponse<C>();
        entityResponse.setEntries(converted);
        entityResponse.setResponseCount(queryResult.getResponseCount());
        entityResponse.setStart(start);
        entityResponse.setTotalCount(queryResult.getTotalCount());

        return Response.ok(entityResponse).build();

    }

    @GET()
    @Path("{id}")
    @Transactional(readOnly = true)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItem(@PathParam("entity") String entity,
                            @PathParam("id") String id,
                            @QueryParam("fields") String fields,
                            @QueryParam("version")  Integer version)

    {
        EntityPlugin<C,P,ID> plugin = getEntityPlugin(entity);
        ApiVersionPlugin<C,P> apiVersionPlugin = plugin.getApiVersionRegistry().getPluginForVersion(version);

        RequestContext requestContext = requestContextBuilder.buildRequestContext(uriInfo,entity,fields);

        List<ID> ids = plugin.getKeyConverter().covertKeys(id);
        List<P> persistent = plugin.getPersistenceOperations().findByIds(ids, requestContext);

        List<C> converted = apiVersionPlugin.getTranslator().convertPersistent(persistent,requestContext);

        Object response;
        if(converted.size() == 1)
            response = converted.get(0);
        else
        {
            EntityResponse<C> entityResponse = new EntityResponse<C>();
            entityResponse.setEntries(converted);
            entityResponse.setResponseCount(converted.size());
            entityResponse.setStart(1);
            entityResponse.setTotalCount(new Long(converted.size()));
            response = entityResponse;
        }

        return Response.ok(response).build();

    }

    @POST
    @Transactional(readOnly = false)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createItem(@PathParam("entity") String entity,
                               @QueryParam("fields") String fields,
                               @QueryParam("version")  Integer version,
                               EntityRequest<C> entityRequest)
    {
        EntityPlugin<C,P,ID> plugin = getEntityPlugin(entity);
        ApiVersionPlugin<C,P> apiVersionPlugin = plugin.getApiVersionRegistry().getPluginForVersion(version);

        RequestContext requestContext = requestContextBuilder.buildRequestContext(uriInfo,entity,fields);

        Set<String> fieldSet = requestContext.getRequestedFields();
        if(fieldSet != null)
            fieldSet.add("id");

        apiVersionPlugin.getValidator().validateCreate(entityRequest.getItem());

        Translator<C,P> translator = apiVersionPlugin.getTranslator();
        P persistent = translator.convertClient(entityRequest.getItem(), requestContext);
        P saved = plugin.getPersistenceOperations().createItem(persistent, requestContext);
        if(saved != null)
        {
            C toReturn = translator.convertPersistent(saved,requestContext);
            return Response.created(URI.create(toReturn.getId().toString())).entity(toReturn).build();
        }

        return Response.notModified().build();

    }

    @PUT
    @Transactional(readOnly = false)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateItem(@PathParam("entity") String entity,
                               @QueryParam("fields") String fields,
                               @QueryParam("version")  Integer version,
                               EntityRequest<C> entityRequest)
    {
        EntityPlugin<C,P,ID> plugin = getEntityPlugin(entity);
        ApiVersionPlugin<C,P> apiVersionPlugin = plugin.getApiVersionRegistry().getPluginForVersion(version);

        if(entityRequest == null || entityRequest.getItem() == null || entityRequest.getItem().getId() == null)
            throw new BadRequestException("Missing payload");

        RequestContext requestContext = requestContextBuilder.buildRequestContext(uriInfo,entity,fields);

        Set<String> fieldSet = requestContext.getRequestedFields();
        if(fieldSet != null)
            fieldSet.add("id");

        C item = entityRequest.getItem();
        ID id = (ID) item.getId();

        PersistenceOperations<P,ID> persistenceOperations = plugin.getPersistenceOperations();
        P existing = persistenceOperations.findById(id, requestContext);
        if(existing == null)
            throw new NotFoundException(
                    String.format("%s with id %s was not found.",entity,entityRequest.getItem().getId()));

        apiVersionPlugin.getValidator().validateUpdate(entityRequest.getItem(),existing);

        Translator<C,P> translator = apiVersionPlugin.getTranslator();
        translator.copyClient(entityRequest.getItem(), existing,requestContext);
        P saved = plugin.getPersistenceOperations().updateItem(existing, requestContext);

        if(saved != null)
        {
            ApiObject toReturn = translator.convertPersistent(saved,requestContext);
            return Response.ok(toReturn).build();
        }

        return Response.notModified().build();
    }

    @DELETE()
    @Path("{id}")
    @Transactional(readOnly = false)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteItem(@PathParam("entity") String entity,
                            @PathParam("id") String id)

    {
        EntityPlugin<C,P,ID> plugin = getEntityPlugin(entity);

        RequestContext requestContext = requestContextBuilder.buildRequestContext(uriInfo,entity,null);

        List<ID> ids = plugin.getKeyConverter().covertKeys(id);
        List<P> persistentItems = plugin.getPersistenceOperations().findByIds(ids, requestContext);
        int deleted = 0;
        for (P p : persistentItems)
        {
            deleted+=plugin.getPersistenceOperations().deleteItem(p, requestContext);
        }

        DeleteResponse response = new DeleteResponse();
        response.setCount(deleted);

        return Response.ok(response).build();

    }


    private EntityPlugin<C,P,ID> getEntityPlugin(String entity)
    {
        EntityPlugin<C,P,ID> plugin = serviceRegistry.getPluginForName(entity);
        if(plugin == null)
            throw new NotFoundException(String.format("%s is not a valid entity.",entity));

        return plugin;
    }


}
