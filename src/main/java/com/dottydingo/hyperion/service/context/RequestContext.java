package com.dottydingo.hyperion.service.context;


import javax.ws.rs.core.UriInfo;
import java.util.Set;

/**
 */
public interface RequestContext
{
    UriInfo getUriInfo();

    String getEntity();

    Set<String> getRequestedFields();

    AuthorizationContext getAuthorizationContext();


}
