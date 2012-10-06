package com.dottydingo.hyperion.service.context;

/**
 * User: mark
 * Date: 10/6/12
 * Time: 11:00 AM
 */
public class EmptyAuthorizationContext implements AuthorizationContext
{
    @Override
    public String getUserString()
    {
        return "unset";
    }
}
