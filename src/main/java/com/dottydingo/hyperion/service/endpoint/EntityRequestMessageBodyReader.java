package com.dottydingo.hyperion.service.endpoint;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.service.configuration.ApiVersionPlugin;
import com.dottydingo.hyperion.service.configuration.EntityPlugin;
import com.dottydingo.hyperion.service.configuration.ServiceRegistry;
import com.dottydingo.hyperion.service.exception.ServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

/**
*/
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class EntityRequestMessageBodyReader implements MessageBodyReader<EntityRequest>
{
    @Context
    private UriInfo uriInfo;

    private ServiceRegistry serviceRegistry;
    private ObjectMapper objectMapper;

    public void setServiceRegistry(ServiceRegistry serviceRegistry)
    {
        this.serviceRegistry = serviceRegistry;
    }

    public void setObjectMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return EntityRequest.class.equals(type);
    }

    @Override
    public EntityRequest readFrom(Class<EntityRequest> type, Type genericType, Annotation[] annotations,
                                  MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
                                  InputStream entityStream) throws IOException, WebApplicationException
    {
        List<String> entities = uriInfo.getPathParameters().get("entity");
        if(entities == null || entities.size() != 1)
        {
            throw new ServiceException("Invalid request.");
        }

        String entity = entities.get(0);
        EntityPlugin plugin = serviceRegistry.getPluginForName(entity);
        if(plugin == null)
            throw new ServiceException(404,String.format("%s is not a valid endpoint.",entity));

        String versionString = uriInfo.getQueryParameters().getFirst("version");
        Integer version = null;
        try
        {
            version = Integer.parseInt(versionString);
        }
        catch (NumberFormatException ignore){}

        ApiVersionPlugin apiVersionPlugin = plugin.getApiVersionRegistry().getPluginForVersion(version);

        try
        {
            ApiObject value = (ApiObject) objectMapper.readValue(entityStream,apiVersionPlugin.getApiClass());
            EntityRequest entityRequest = new EntityRequest();
            entityRequest.setItem(value);
            return entityRequest;
        }
        catch (Exception e)
        {
            throw new ServiceException(String.format("Error unmarshalling %s",entity));
        }

    }
}
