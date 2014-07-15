package com.dottydingo.hyperion.core.translation;

import com.dottydingo.hyperion.core.persistence.PersistenceContext;

/**
 */
public interface ValueConverter<C,P>
{
    public C convertToClientValue(P persistentValue, PersistenceContext context);

    public P convertToPersistentValue(C clientValue, PersistenceContext context);
}
