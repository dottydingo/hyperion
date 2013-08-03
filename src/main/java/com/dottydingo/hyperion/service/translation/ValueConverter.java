package com.dottydingo.hyperion.service.translation;

import com.dottydingo.hyperion.service.persistence.PersistenceContext;

/**
 */
public interface ValueConverter<C,P>
{
    public C convertToClientValue(P persistentValue, PersistenceContext context);

    public P convertToPersistentValue(C clientValue, PersistenceContext context);
}
