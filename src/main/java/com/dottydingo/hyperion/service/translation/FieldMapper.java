package com.dottydingo.hyperion.service.translation;

import com.dottydingo.hyperion.service.context.RequestContext;

/**
 */
public interface FieldMapper <C,P>
{
    String getClientFieldName();

    void convertToClient(P persistentObject, C clientObject, RequestContext context);

    void convertToPersistent(C clientObject, P persistentObject, RequestContext context);
}
