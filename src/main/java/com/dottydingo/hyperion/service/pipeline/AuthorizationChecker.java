package com.dottydingo.hyperion.service.pipeline;

import com.dottydingo.hyperion.exception.AuthorizationException;
import com.dottydingo.service.endpoint.context.EndpointContext;

/**
 */
public interface AuthorizationChecker<C extends EndpointContext>
{
    void checkAuthorization(C endpointContext) throws AuthorizationException;
}
