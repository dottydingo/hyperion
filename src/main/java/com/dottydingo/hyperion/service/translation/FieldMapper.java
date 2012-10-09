package com.dottydingo.hyperion.service.translation;

import com.dottydingo.hyperion.service.context.RequestContext;

/**
 */
public interface FieldMapper <C,P>
{
    String getClientFieldName();

    void convertToClient(ObjectWrapper<P> persistentObjectWrapper, ObjectWrapper<C> clientObjectWrapper, RequestContext context);

    void convertToPersistent(ObjectWrapper<C> clientObjectWrapper, ObjectWrapper<P> persistentObjectWrapper, RequestContext context);
}
