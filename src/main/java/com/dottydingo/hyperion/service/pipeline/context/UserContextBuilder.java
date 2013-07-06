package com.dottydingo.hyperion.service.pipeline.context;

import com.dottydingo.hyperion.service.context.UserContext;

/**
 */
public interface UserContextBuilder<C extends HyperionContext>
{
    UserContext buildUserContext(C context);
}
