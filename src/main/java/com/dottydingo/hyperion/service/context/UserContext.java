package com.dottydingo.hyperion.service.context;

import java.security.Principal;

/**
 */
public interface UserContext
{
    String getUserIdentifier();
    Principal getPrincipal();
}
