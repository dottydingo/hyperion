package com.dottydingo.hyperion.core.endpoint.marshall;

import com.dottydingo.hyperion.api.exception.BadRequestException;
import com.dottydingo.hyperion.api.exception.InternalException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.io.OutputStream;

/**
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

    public <T> T unmarshall(InputStream inputStream, Class<T> type) throws MarshallingException
    {
        try
        {
            return objectMapper.readValue(inputStream,type);
        }
        catch (Exception e)
        {
            throw new MarshallingException(e);
        }
    }

    public <T> RequestContext<T> unmarshallWithContext(InputStream inputStream, Class<T> type)  throws MarshallingException
    {
        try
        {
            JsonNode jsonNode = objectMapper.readTree(inputStream);
            T value = objectMapper.convertValue(jsonNode,type);

            return new RequestContext<T>(value,jsonNode.fieldNames());
        }
        catch (Exception e)
        {
            throw new MarshallingException(e);
        }
    }


    public <T> void marshall(OutputStream outputStream, T value) throws MarshallingException
    {
        try
        {
            objectMapper.writeValue(outputStream,value);
        }
        catch(Exception e)
        {
            throw new MarshallingException(e);
        }

    }
}
