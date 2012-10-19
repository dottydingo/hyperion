package com.dottydingo.hyperion.service.endpoint;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.exception.AuthorizationException;
import com.dottydingo.hyperion.exception.BadRequestException;
import com.dottydingo.hyperion.exception.HyperionException;
import com.dottydingo.hyperion.exception.NotFoundException;
import com.dottydingo.hyperion.service.context.RequestContext;
import com.dottydingo.hyperion.service.context.RequestContextBuilder;
import com.dottydingo.hyperion.service.marshall.EndpointMarshaller;
import com.dottydingo.hyperion.service.model.PersistentObject;
import com.dottydingo.hyperion.service.persistence.PersistenceOperations;
import com.dottydingo.hyperion.service.persistence.QueryResult;
import com.dottydingo.hyperion.service.configuration.ApiVersionPlugin;
import com.dottydingo.hyperion.service.configuration.EntityPlugin;
import com.dottydingo.hyperion.service.configuration.ServiceRegistry;
import com.dottydingo.hyperion.service.translation.Translator;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
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
    private EndpointAuthorizationChecker endpointAuthorizationChecker = new EmptyAuthorizationChecker();
    private EndpointMarshaller endpointMarshaller = new EndpointMarshaller();
    private EndpointExceptionHandler endpointExceptionHandler = new DefaultEndpointExceptionHandler();

    @Context
    private UriInfo uriInfo;
    @Context
    private HttpServletRequest httpServletRequest;
    @Context
    private HttpServletResponse httpServletResponse;


    public void setServiceRegistry(ServiceRegistry serviceRegistry)
    {
        this.serviceRegistry = serviceRegistry;
    }

    public void setRequestContextBuilder(RequestContextBuilder requestContextBuilder)
    {
        this.requestContextBuilder = requestContextBuilder;
    }

    public void setEndpointAuthorizationChecker(EndpointAuthorizationChecker endpointAuthorizationChecker)
    {
        this.endpointAuthorizationChecker = endpointAuthorizationChecker;
    }

    public void setEndpointMarshaller(EndpointMarshaller endpointMarshaller)
    {
        this.endpointMarshaller = endpointMarshaller;
    }

    public void setEndpointExceptionHandler(EndpointExceptionHandler endpointExceptionHandler)
    {
        this.endpointExceptionHandler = endpointExceptionHandler;
    }

    @GET()
    @Transactional(readOnly = true)
    public Response queryData(@PathParam("entity") String entity,
                              @QueryParam("fields") String fields,
                              @QueryParam("start")  Integer start,
                              @QueryParam("limit")  Integer limit,
                              @QueryParam("query") String query,
                              @QueryParam("sort") String sort,
                              @QueryParam("version")  Integer version)
    {
        RequestContext requestContext = requestContextBuilder.buildRequestContext(uriInfo,httpServletRequest,
                httpServletResponse,entity, fields);

        try
        {
            EntityPlugin<C,P,ID> plugin = getEntityPlugin(entity);
            checkMethodAllowed(plugin,HttpMethod.GET);

            ApiVersionPlugin<C,P> apiVersionPlugin = plugin.getApiVersionRegistry().getPluginForVersion(version);

            endpointAuthorizationChecker.checkAuthorization(requestContext);

            QueryResult<P> queryResult = plugin.getPersistenceOperations().query(query, start, limit, sort, requestContext);
            List<P> persistent = queryResult.getItems();

            List<C> converted = apiVersionPlugin.getTranslator().convertPersistent(persistent,requestContext);

            EntityResponse<C> entityResponse = new EntityResponse<C>();
            entityResponse.setEntries(converted);
            entityResponse.setResponseCount(queryResult.getResponseCount());
            entityResponse.setStart(start);
            entityResponse.setTotalCount(queryResult.getTotalCount());

            endpointMarshaller.marshall(httpServletResponse,entityResponse);

            return Response.ok().build();
        }
        catch (Throwable t)
        {
            return endpointExceptionHandler.handleException(t,endpointMarshaller,requestContext);
        }

    }

    @GET()
    @Path("{id}")
    @Transactional(readOnly = true)
    public Response getItem(@PathParam("entity") String entity,
                            @PathParam("id") String id,
                            @QueryParam("fields") String fields,
                            @QueryParam("version")  Integer version)

    {
        RequestContext requestContext = requestContextBuilder.buildRequestContext(uriInfo,httpServletRequest,
                httpServletResponse,entity, fields);

        try
        {
            EntityPlugin<C,P,ID> plugin = getEntityPlugin(entity);
            checkMethodAllowed(plugin,HttpMethod.GET);

            ApiVersionPlugin<C,P> apiVersionPlugin = plugin.getApiVersionRegistry().getPluginForVersion(version);

            endpointAuthorizationChecker.checkAuthorization(requestContext);

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

            endpointMarshaller.marshall(httpServletResponse,response);

            return Response.ok().build();
        }
        catch (Throwable t)
        {
            return endpointExceptionHandler.handleException(t,endpointMarshaller,requestContext);
        }

    }

    @POST
    @Transactional(readOnly = false)
    public Response createItem(@PathParam("entity") String entity,
                               @QueryParam("fields") String fields,
                               @QueryParam("version")  Integer version                               )
    {
        RequestContext requestContext = requestContextBuilder.buildRequestContext(uriInfo,httpServletRequest,
                httpServletResponse,entity, fields);
        try
        {
            EntityPlugin<C,P,ID> plugin = getEntityPlugin(entity);
            checkMethodAllowed(plugin,HttpMethod.POST);
            ApiVersionPlugin<C,P> apiVersionPlugin = plugin.getApiVersionRegistry().getPluginForVersion(version);

            endpointAuthorizationChecker.checkAuthorization(requestContext);

            C clientObject = endpointMarshaller.unmarshall(httpServletRequest,apiVersionPlugin.getApiClass());
            Set<String> fieldSet = requestContext.getRequestedFields();
            if(fieldSet != null)
                fieldSet.add("id");

            apiVersionPlugin.getValidator().validateCreate(clientObject);

            Translator<C,P> translator = apiVersionPlugin.getTranslator();
            P persistent = translator.convertClient(clientObject, requestContext);
            P saved = plugin.getPersistenceOperations().createItem(persistent, requestContext);
            if(saved != null)
            {
                C toReturn = translator.convertPersistent(saved,requestContext);
                endpointMarshaller.marshall(httpServletResponse,toReturn);
                return Response.created(URI.create(toReturn.getId().toString())).build();
            }

            return Response.notModified().build();
        }
        catch (Throwable t)
        {
            return endpointExceptionHandler.handleException(t,endpointMarshaller,requestContext);
        }

    }

    @PUT
    @Transactional(readOnly = false)
    public Response updateItem(@PathParam("entity") String entity,
                               @QueryParam("fields") String fields,
                               @QueryParam("version")  Integer version)
    {
        RequestContext requestContext = requestContextBuilder.buildRequestContext(uriInfo,httpServletRequest,
                httpServletResponse,entity, fields);
        try
        {
            EntityPlugin<C,P,ID> plugin = getEntityPlugin(entity);
            checkMethodAllowed(plugin,HttpMethod.PUT);
            ApiVersionPlugin<C,P> apiVersionPlugin = plugin.getApiVersionRegistry().getPluginForVersion(version);

            C clientObject = endpointMarshaller.unmarshall(httpServletRequest,apiVersionPlugin.getApiClass());

            if( clientObject.getId() == null)
                throw new BadRequestException("Missing payload");

            endpointAuthorizationChecker.checkAuthorization(requestContext);

            Set<String> fieldSet = requestContext.getRequestedFields();
            if(fieldSet != null)
                fieldSet.add("id");

            ID id = (ID) clientObject.getId();

            PersistenceOperations<P,ID> persistenceOperations = plugin.getPersistenceOperations();
            P existing = persistenceOperations.findById(id, requestContext);
            if(existing == null)
                throw new NotFoundException(
                        String.format("%s with id %s was not found.",entity,clientObject.getId()));

            apiVersionPlugin.getValidator().validateUpdate(clientObject,existing);

            Translator<C,P> translator = apiVersionPlugin.getTranslator();
            translator.copyClient(clientObject, existing,requestContext);
            P saved = plugin.getPersistenceOperations().updateItem(existing, requestContext);

            if(saved != null)
            {
                ApiObject toReturn = translator.convertPersistent(saved,requestContext);
                endpointMarshaller.marshall(httpServletResponse,toReturn);
                return Response.ok().build();
            }

            return Response.notModified().build();
        }
        catch (Throwable t)
        {
            return endpointExceptionHandler.handleException(t,endpointMarshaller,requestContext);
        }
    }

    @DELETE()
    @Path("{id}")
    @Transactional(readOnly = false)
    public Response deleteItem(@PathParam("entity") String entity,
                            @PathParam("id") String id)
    {
        RequestContext requestContext = requestContextBuilder.buildRequestContext(uriInfo,httpServletRequest,
                httpServletResponse,entity);

        try
        {
            EntityPlugin<C,P,ID> plugin = getEntityPlugin(entity);
            checkMethodAllowed(plugin,HttpMethod.DELETE);

            endpointAuthorizationChecker.checkAuthorization(requestContext);

            List<ID> ids = plugin.getKeyConverter().covertKeys(id);
            List<P> persistentItems = plugin.getPersistenceOperations().findByIds(ids, requestContext);
            int deleted = 0;
            for (P p : persistentItems)
            {
                deleted+=plugin.getPersistenceOperations().deleteItem(p, requestContext);
            }

            DeleteResponse response = new DeleteResponse();
            response.setCount(deleted);
            endpointMarshaller.marshall(httpServletResponse,deleted);

            return Response.ok(response).build();
        }
        catch (Throwable t)
        {
            return endpointExceptionHandler.handleException(t,endpointMarshaller,requestContext);
        }

    }


    private EntityPlugin<C,P,ID> getEntityPlugin(String entity)
    {
        EntityPlugin<C,P,ID> plugin = serviceRegistry.getPluginForName(entity);
        if(plugin == null)
            throw new NotFoundException(String.format("%s is not a valid entity.",entity));

        return plugin;
    }

    private void checkMethodAllowed(EntityPlugin plugin, HttpMethod httpMethod)
    {
        if(!plugin.isMethodAllowed(httpMethod))
            throw new HyperionException(405,String.format("%s is not allowed.",httpMethod));
    }


    private class EmptyAuthorizationChecker implements EndpointAuthorizationChecker
    {
        @Override
        public void checkAuthorization(RequestContext requestContext) throws AuthorizationException {}
    }
}
