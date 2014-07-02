package com.dottydingo.hyperion.core.endpoint.pipeline.auth;

/**
*/
public class NoOpAuthorizationContext implements AuthorizationContext
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
