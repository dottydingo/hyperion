package com.dottydingo.hyperion.service.marshall;

import com.dottydingo.hyperion.exception.BadRequestException;
import com.dottydingo.hyperion.exception.InternalException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.OutputStream;

/**
 * User: mark
 * Date: 10/17/12
 * Time: 8:23 PM
 */
public class EndpointMarshaller
{
    private ObjectMapper objectMapper;

    public EndpointMarshaller()
    {
        try
        {
            objectMapper = new ObjectMapperBuilder().getObject();
        }
        catch (Exception ignore){}
    }

    public void setObjectMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public <T> T unmarshall(HttpServletRequest httpServletRequest, Class<T> type)
    {
        try
        {
            return objectMapper.readValue(httpServletRequest.getInputStream(),type);
        }
        catch (Exception e)
        {
            throw new BadRequestException("Error unmarshalling request.",e);
        }
    }

    public <T> void marshall(HttpServletResponse httpServletResponse, T value)
    {
        try
        {
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON);
            httpServletResponse.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(httpServletResponse.getOutputStream(),value);
        }
        catch(Exception e)
        {
            throw new InternalException("Error marhsalling response.",e);
        }

    }

    public <T> void marshall(OutputStream outputStream, T value)
    {
        try
        {
            objectMapper.writeValue(outputStream,value);
        }
        catch(Exception e)
        {
            throw new InternalException("Error marhsalling response.",e);
        }

    }
}
