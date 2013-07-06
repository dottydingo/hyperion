package com.dottydingo.hyperion.service.context;

/**
 */
public interface UserContextBuilder<C extends HyperionContext>
{
    UserContext buildUserContext(C context);
}
