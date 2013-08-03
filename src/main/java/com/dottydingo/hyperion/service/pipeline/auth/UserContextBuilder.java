package com.dottydingo.hyperion.service.pipeline.auth;

import com.dottydingo.hyperion.service.context.HyperionContext;

/**
 */
public interface UserContextBuilder
{
    UserContext buildUserContext(HyperionContext context);
}
