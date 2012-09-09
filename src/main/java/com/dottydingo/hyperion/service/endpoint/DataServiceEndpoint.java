package com.dottydingo.hyperion.service.endpoint;

import com.dottydingo.hyperion.api.BaseApiObject;
import com.dottydingo.hyperion.service.configuration.ApiVersionPlugin;
import com.dottydingo.hyperion.service.configuration.EntityPlugin;
import com.dottydingo.hyperion.service.configuration.ServiceRegistry;
import com.dottydingo.hyperion.service.exception.ServiceException;
import com.dottydingo.hyperion.service.model.BasePersistentObject;
import com.dottydingo.hyperion.service.translation.DefaultTranslationContext;
import com.dottydingo.hyperion.service.translation.Translator;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 */
@Path("/data/{entity}")
public class DataServiceEndpoint<C extends BaseApiObject,P extends BasePersistentObject>
{
    private ServiceRegistry serviceRegistry;

    public void setServiceRegistry(ServiceRegistry serviceRegistry)
    {
        this.serviceRegistry = serviceRegistry;
    }

    @GET()
    @Path("/")
    @Transactional(readOnly = true)
    @Produces(MediaType.APPLICATION_JSON)
    public Response queryData(@PathParam("entity") String entity,
                              @QueryParam("fields") String fields,
                              @QueryParam("start") Integer start,
                              @QueryParam("limit") Integer limit,
                              @QueryParam("query") String query,
                              @QueryParam("version")  Integer version)
    {
        EntityPlugin<C,P> plugin = getEntityPlugin(entity);
        ApiVersionPlugin<C,P> apiVersionPlugin = plugin.getApiVersionRegistry().getPluginForVersion(version);

        Page<P> page = plugin.getQueryHandler().processQuery(query,start,limit);
        List<P> persistent = page.getContent();

        Set<String> fieldSet = buildFieldSet(fields);
        DefaultTranslationContext translationContext = new DefaultTranslationContext();
        translationContext.setRequestedFields(fieldSet);

        List<C> converted = apiVersionPlugin.getTranslator().convertPersistent(persistent,translationContext);

        EntityResponse<C> entityResponse = new EntityResponse<C>();
        entityResponse.setEntries(converted);
        entityResponse.setPageSize(page.getSize());
        entityResponse.setStart(page.getNumber() * page.getSize());
        entityResponse.setTotalCount(page.getTotalElements());

        return Response.ok(entityResponse).build();

    }

    @GET()
    @Path("/{id}")
    @Transactional(readOnly = true)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItem(@PathParam("entity") String entity,
                            @PathParam("id") String id,
                            @QueryParam("fields") String fields,
                            @QueryParam("version")  Integer version)

    {
        EntityPlugin<C,P> plugin = getEntityPlugin(entity);
        ApiVersionPlugin<C,P> apiVersionPlugin = plugin.getApiVersionRegistry().getPluginForVersion(version);

        P persistent = plugin.getJpaRepository().findOne(new Long(id));

        Set<String> fieldSet = buildFieldSet(fields);
        DefaultTranslationContext translationContext = new DefaultTranslationContext();
        translationContext.setRequestedFields(fieldSet);

        Object converted = apiVersionPlugin.getTranslator().convertPersistent(persistent,translationContext);

        return Response.ok(converted).build();

    }

    @POST
    @Path("/")
    @Transactional(readOnly = false)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createItem(@PathParam("entity") String entity,
                               @QueryParam("fields") String fields,
                               @QueryParam("version")  Integer version,
                               EntityRequest<C> entityRequest)
    {
        EntityPlugin<C,P> plugin = getEntityPlugin(entity);
        ApiVersionPlugin<C,P> apiVersionPlugin = plugin.getApiVersionRegistry().getPluginForVersion(version);

        Set<String> fieldSet = buildFieldSet(fields);
        if(fieldSet != null)
            fieldSet.add("id");
        DefaultTranslationContext translationContext = new DefaultTranslationContext();
        translationContext.setRequestedFields(fieldSet);

        apiVersionPlugin.getValidator().validateCreate(entityRequest.getItem());

        Translator<C,P> translator = apiVersionPlugin.getTranslator();
        P persistent = translator.convertClient(entityRequest.getItem(), translationContext);
        P saved = plugin.getJpaRepository().save(persistent);

        C toReturn = translator.convertPersistent(saved,translationContext);

        return Response.created(URI.create(toReturn.getId().toString())).entity(toReturn).build();

    }

    @PUT
    @Path("/")
    @Transactional(readOnly = false)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateItem(@PathParam("entity") String entity,
                               @QueryParam("fields") String fields,
                               @QueryParam("version")  Integer version,
                               EntityRequest<C> entityRequest)
    {
        EntityPlugin<C,P> plugin = getEntityPlugin(entity);
        ApiVersionPlugin<C,P> apiVersionPlugin = plugin.getApiVersionRegistry().getPluginForVersion(version);

        if(entityRequest == null || entityRequest.getItem() == null || entityRequest.getItem().getId() == null)
            throw new ServiceException(400,"Missing payload");

        Set<String> fieldSet = buildFieldSet(fields);
        if(fieldSet != null)
            fieldSet.add("id");

        DefaultTranslationContext translationContext = new DefaultTranslationContext();
        translationContext.setRequestedFields(fieldSet);

        P existing = plugin.getJpaRepository().findOne(entityRequest.getItem().getId());
        if(existing == null)
            throw new ServiceException(404,
                    String.format("%s with id %d was not found.",entity,entityRequest.getItem().getId()));

        apiVersionPlugin.getValidator().validateUpdate(entityRequest.getItem(),existing);

        Translator<C,P> translator = apiVersionPlugin.getTranslator();
        translator.copyClient(entityRequest.getItem(), existing,translationContext);
        P saved = plugin.getJpaRepository().save(existing);

        C toReturn = translator.convertPersistent(saved,translationContext);

        return Response.ok(toReturn).build();

    }

    @DELETE()
    @Path("/{id}")
    @Transactional(readOnly = true)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteItem(@PathParam("entity") String entity,
                            @PathParam("id") String id)

    {
        EntityPlugin<C,P> plugin = getEntityPlugin(entity);

        P persistent = plugin.getJpaRepository().findOne(new Long(id));

        plugin.getJpaRepository().delete(persistent);

        return Response.ok().build();

    }


    private EntityPlugin<C,P> getEntityPlugin(String entity)
    {
        EntityPlugin<C,P> plugin = serviceRegistry.getPluginForName(entity);
        if(plugin == null)
            throw new ServiceException(404,String.format("%s is not a valid entity.",entity));

        return plugin;
    }

    private Set<String> buildFieldSet(String fields)
    {
        if(fields == null || fields.length() == 0)
            return null;

        String[] split = fields.split(",");
        Set<String> fieldSet = new LinkedHashSet<String>();
        for (String s : split)
        {
            fieldSet.add(s.trim());
        }

        return fieldSet;
    }
}
