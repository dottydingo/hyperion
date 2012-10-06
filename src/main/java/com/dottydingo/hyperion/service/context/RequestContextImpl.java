package com.dottydingo.hyperion.service.context;


import javax.ws.rs.core.UriInfo;
import java.util.Set;

/**
 * Default Request Context Implementation
 */
public class RequestContextImpl implements RequestContext
{
    private UriInfo uriInfo;
    private String entity;
    private AuthorizationContext authorizationContext;
    private Set<String> requestedFields;

    public UriInfo getUriInfo()
    {
        return uriInfo;
    }

    public void setUriInfo(UriInfo uriInfo)
    {
        this.uriInfo = uriInfo;
    }

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

    public AuthorizationContext getAuthorizationContext()
    {
        return authorizationContext;
    }

    public void setAuthorizationContext(AuthorizationContext authorizationContext)
    {
        this.authorizationContext = authorizationContext;
    }

}
