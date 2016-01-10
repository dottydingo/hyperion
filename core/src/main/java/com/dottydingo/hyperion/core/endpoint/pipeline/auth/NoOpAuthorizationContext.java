package com.dottydingo.hyperion.core.endpoint.pipeline.auth;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.core.model.PersistentObject;
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
    public boolean isReadable(PersistentObject persistent, String propertyName)
    {
        return true;
    }

    @Override
    public boolean isWritableOnCreate(ApiObject client, String propertyName)
    {
        return true;
    }

    @Override
    public boolean isWritableOnUpdate(ApiObject client, PersistentObject persistent, String propertyName)
    {
        return true;
    }
}
