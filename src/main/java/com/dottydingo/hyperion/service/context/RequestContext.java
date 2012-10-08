package com.dottydingo.hyperion.service.context;


import javax.ws.rs.core.UriInfo;
import java.security.Principal;
import java.util.Set;

/**
 */
public interface RequestContext
{
    UriInfo getUriInfo();

    String getEntity();

    Set<String> getRequestedFields();

    Principal getPrincipal();

    String getUserIdentifier();
}
