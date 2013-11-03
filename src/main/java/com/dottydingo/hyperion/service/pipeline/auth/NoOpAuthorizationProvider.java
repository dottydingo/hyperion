package com.dottydingo.hyperion.service.pipeline.auth;

import com.dottydingo.hyperion.exception.AuthorizationException;
import com.dottydingo.hyperion.service.context.HyperionContext;

/**
 */
public class NoOpAuthorizationProvider implements AuthorizationProvider
{
    @Override
    public AuthorizationContext authorize(HyperionContext context) throws AuthorizationException
    {
        return new NoOpAuthorizationContext();
    }

    private class NoOpAuthorizationContext implements AuthorizationContext
    {
        @Override
        public boolean isAuthorized()
        {
            return true;
        }

        @Override
        public boolean isReadable(String propertyName)
        {
            return true;
        }

        @Override
        public boolean isWritable(String propertyName)
        {
            return true;
        }
    }
}
