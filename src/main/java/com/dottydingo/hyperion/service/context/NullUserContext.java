package com.dottydingo.hyperion.service.context;

import java.security.Principal;

/**
 */
public class NullUserContext implements UserContext
{
    private Principal principal = new NullPrincipal();

    @Override
    public String getUserIdentifier()
    {
        return "";
    }

    @Override
    public Principal getPrincipal()
    {
        return principal;
    }

    private class NullPrincipal implements Principal
    {
        @Override
        public String getName()
        {
            return "";
        }
    }
}
