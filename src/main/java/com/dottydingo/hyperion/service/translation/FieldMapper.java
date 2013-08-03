package com.dottydingo.hyperion.service.translation;

import com.dottydingo.hyperion.service.persistence.PersistenceContext;

/**
 */
public interface FieldMapper <C,P>
{
    String getClientFieldName();

    void convertToClient(ObjectWrapper<P> persistentObjectWrapper, ObjectWrapper<C> clientObjectWrapper, PersistenceContext context);

    void convertToPersistent(ObjectWrapper<C> clientObjectWrapper, ObjectWrapper<P> persistentObjectWrapper, PersistenceContext context);
}
