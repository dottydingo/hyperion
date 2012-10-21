package com.dottydingo.hyperion.service.context;

import java.security.Principal;

/**
 * User: mark
 * Date: 10/21/12
 * Time: 4:24 PM
 */
public interface UserContext
{
    String getUserIdentifier();
    Principal getPrincipal();
}
