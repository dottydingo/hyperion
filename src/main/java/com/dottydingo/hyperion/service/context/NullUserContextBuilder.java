package com.dottydingo.hyperion.service.context;

/**
 */
public class NullUserContextBuilder implements UserContextBuilder<HyperionContext>
{
    @Override
    public UserContext buildUserContext(HyperionContext context)
    {
        return new NullUserContext();
    }
}
