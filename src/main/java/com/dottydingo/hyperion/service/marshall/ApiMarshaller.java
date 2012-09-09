package com.dottydingo.hyperion.service.marshall;

import com.dottydingo.hyperion.api.BaseApiObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 */
public interface ApiMarshaller<T extends BaseApiObject>
{
    Class<T> getApiType();
    T unmarshallItem(InputStream inputStream) throws Exception;
    void marshallItem(T item, OutputStream outputStream) throws Exception;
    void marshallItems(List<T> items,OutputStream outputStream) throws Exception;
}
