package com.dottydingo.hyperion.service.pipeline.context;

import com.dottydingo.hyperion.service.context.NullUserContext;
import com.dottydingo.hyperion.service.context.UserContext;

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
