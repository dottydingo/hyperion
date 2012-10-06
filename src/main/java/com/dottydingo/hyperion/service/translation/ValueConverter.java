package com.dottydingo.hyperion.service.translation;

import com.dottydingo.hyperion.service.context.RequestContext;

/**
 */
public interface ValueConverter<C,P>
{
    public C convertToClientValue(P persistentValue, RequestContext context);

    public P convertToPersistentValue(C clientValue, RequestContext context);
}
