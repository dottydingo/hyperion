package com.dottydingo.hyperion.core.translation;

import com.dottydingo.hyperion.core.persistence.PersistenceContext;

import java.io.Serializable;

/**
 */
public interface IdFieldMapper<C,P> extends FieldMapper<C,P>
{
    <ID extends Serializable> ID convertId(ObjectWrapper<C> client, PersistenceContext context);
}
