package com.dottydingo.hyperion.service.pipeline.auth;

import com.dottydingo.hyperion.exception.AuthorizationException;
import com.dottydingo.hyperion.service.context.HyperionContext;

/**
 */
public class NoOpAuthorizationChecker implements AuthorizationChecker
{
    @Override
    public void checkAuthorization(HyperionContext context) throws AuthorizationException
    {

    }
}
