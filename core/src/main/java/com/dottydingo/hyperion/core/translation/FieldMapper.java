package com.dottydingo.hyperion.core.translation;

import com.dottydingo.hyperion.core.persistence.PersistenceContext;

/**
 */
public interface FieldMapper <C,P>
{
    String getClientFieldName();

    void convertToClient(ObjectWrapper<P> persistentObjectWrapper, ObjectWrapper<C> clientObjectWrapper, PersistenceContext context);

    boolean convertToPersistent(ObjectWrapper<C> clientObjectWrapper, ObjectWrapper<P> persistentObjectWrapper, PersistenceContext context);
}
