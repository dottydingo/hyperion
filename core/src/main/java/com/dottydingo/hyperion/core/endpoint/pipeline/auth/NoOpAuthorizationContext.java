package com.dottydingo.hyperion.core.endpoint.pipeline.auth;

import com.dottydingo.service.endpoint.context.UserContext;

/**
*/
public class NoOpAuthorizationContext implements AuthorizationContext
{
    private UserContext userContext;

    public NoOpAuthorizationContext(UserContext userContext)
    {
        this.userContext = userContext;
    }

    @Override
    public UserContext getUserContext()
    {
        return userContext;
    }

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
