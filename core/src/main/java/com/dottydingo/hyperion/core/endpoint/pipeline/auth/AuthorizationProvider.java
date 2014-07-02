package com.dottydingo.hyperion.core.endpoint.pipeline.auth;

import com.dottydingo.hyperion.api.exception.AuthorizationException;
import com.dottydingo.hyperion.core.endpoint.HyperionContext;

/**
 */
public interface AuthorizationProvider
{
    AuthorizationContext authorize(HyperionContext context) throws AuthorizationException;
}
