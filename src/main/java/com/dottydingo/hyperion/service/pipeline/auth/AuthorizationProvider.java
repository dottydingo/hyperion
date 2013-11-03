package com.dottydingo.hyperion.service.pipeline.auth;

import com.dottydingo.hyperion.exception.AuthorizationException;
import com.dottydingo.hyperion.service.context.HyperionContext;
import com.dottydingo.service.endpoint.context.EndpointContext;

/**
 */
public interface AuthorizationProvider
{
    AuthorizationContext authorize(HyperionContext context) throws AuthorizationException;
}
