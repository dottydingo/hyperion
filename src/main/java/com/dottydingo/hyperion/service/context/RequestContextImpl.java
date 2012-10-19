package com.dottydingo.hyperion.service.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.UriInfo;
import java.security.Principal;
import java.util.Set;

/**
 * Default Request Context Implementation
 */
public class RequestContextImpl implements RequestContext
{
    private UriInfo uriInfo;
    private String entity;
    private Set<String> requestedFields;
    private Principal principal;
    private String userIdentifier;
    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;

    @Override
    public Principal getPrincipal()
    {
        return principal;
    }

    @Override
    public String getUserIdentifier()
    {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier)
    {
        this.userIdentifier = userIdentifier;
    }

    @Override
    public UriInfo getUriInfo()
    {
        return uriInfo;
    }

    public void setUriInfo(UriInfo uriInfo)
    {
        this.uriInfo = uriInfo;
    }

    @Override
    public String getEntity()
    {
        return entity;
    }

    @Override
    public Set<String> getRequestedFields()
    {
        return requestedFields;
    }

    public void setRequestedFields(Set<String> requestedFields)
    {
        this.requestedFields = requestedFields;
    }

    public void setEntity(String entity)
    {
        this.entity = entity;
    }

    public void setPrincipal(Principal principal)
    {
        this.principal = principal;
    }

    @Override
    public HttpServletRequest getHttpServletRequest()
    {
        return httpServletRequest;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest)
    {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public HttpServletResponse getHttpServletResponse()
    {
        return httpServletResponse;
    }

    public void setHttpServletResponse(HttpServletResponse httpServletResponse)
    {
        this.httpServletResponse = httpServletResponse;
    }
}
