package com.dottydingo.hyperion.service.pipeline.auth;

import com.dottydingo.hyperion.service.context.HyperionContext;

/**
 */
public class NullUserContextBuilder implements UserContextBuilder
{
    @Override
    public UserContext buildUserContext(HyperionContext context)
    {
        return new NullUserContext();
    }
}
