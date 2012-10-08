package com.dottydingo.hyperion.service.context;

import com.dottydingo.hyperion.service.endpoint.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * User: mark
 * Date: 9/27/12
 * Time: 8:56 PM
 */
public class DefaultRequestContextBuilder implements RequestContextBuilder
{

    @Context
    protected UriInfo uriInfo;

    @Context
    protected HttpServletRequest httpServletRequest;
    private Principal principal;

    @Override
    public RequestContext buildRequestContext(String entityType, HttpMethod httpMethod, String fields)
    {
        RequestContextImpl requestContext = new RequestContextImpl();
        requestContext.setRequestedFields(buildFieldSet(fields));
        requestContext.setEntity(entityType);
        requestContext.setUriInfo(uriInfo);
        requestContext.setUserIdentifier(getUserIdentifier());
        requestContext.setPrincipal(getPrincipal());

        return requestContext;
    }

    @Override
    public RequestContext buildRequestContext(String entityType, HttpMethod httpMethod)
    {
        return buildRequestContext(entityType,httpMethod,null);
    }

    protected String getUserIdentifier()
    {
        return "";
    }

    protected Principal getPrincipal()
    {
        return principal;
    }

    private Set<String> buildFieldSet(String fields)
    {
        if(fields == null || fields.length() == 0)
            return null;

        String[] split = fields.split(",");
        Set<String> fieldSet = new LinkedHashSet<String>();
        for (String s : split)
        {
            fieldSet.add(s.trim());
        }

        return fieldSet;
    }


}
