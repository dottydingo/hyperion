package com.dottydingo.hyperion.service.context;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    HttpServletRequest getHttpServletRequest();

    HttpServletResponse getHttpServletResponse();
}
