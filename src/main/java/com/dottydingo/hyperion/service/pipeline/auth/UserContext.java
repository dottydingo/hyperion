package com.dottydingo.hyperion.service.pipeline.auth;

import java.security.Principal;

/**
 */
public interface UserContext
{
    String getUserIdentifier();
    Principal getPrincipal();
}
