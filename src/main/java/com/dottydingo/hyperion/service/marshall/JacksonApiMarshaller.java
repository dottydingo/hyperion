package com.dottydingo.hyperion.service.marshall;

import com.dottydingo.hyperion.api.BaseApiObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 */
public class JacksonApiMarshaller<T extends BaseApiObject> implements ApiMarshaller<T>
{
    private ObjectMapper objectMapper;
    private Class<T> apiType;

    public void setObjectMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public void setApiType(Class<T> apiType)
    {
        this.apiType = apiType;
    }

    @Override
    public Class<T> getApiType()
    {
        return apiType;
    }

    @Override
    public T unmarshallItem(InputStream inputStream) throws Exception
    {
        return objectMapper.readValue(inputStream,getApiType());
    }

    @Override
    public void marshallItem(T item, OutputStream outputStream)  throws Exception
    {
        objectMapper.writeValue(outputStream,item);
    }

    @Override
    public void marshallItems(List<T> items, OutputStream outputStream) throws Exception
    {
        objectMapper.writeValue(outputStream,items);
    }
}
