package com.dottydingo.hyperion.service.context;

import com.dottydingo.hyperion.service.endpoint.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Override
    public RequestContext buildRequestContext(UriInfo uriInfo,
                                              HttpServletRequest httpServletRequest,
                                              HttpServletResponse httpServletResponse,
                                              String entityType, String fields)
    {
        RequestContextImpl requestContext = new RequestContextImpl();
        requestContext.setRequestedFields(buildFieldSet(fields));
        requestContext.setEntity(entityType);
        requestContext.setUriInfo(uriInfo);
        requestContext.setUserIdentifier(getUserIdentifier(httpServletRequest));
        requestContext.setPrincipal(getPrincipal(httpServletRequest));
        requestContext.setHttpServletRequest(httpServletRequest);
        requestContext.setHttpServletResponse(httpServletResponse);

        return requestContext;
    }

    @Override
    public RequestContext buildRequestContext(UriInfo uriInfo,
                                              HttpServletRequest httpServletRequest,
                                              HttpServletResponse httpServletResponse,
                                              String entityType)
    {
        return buildRequestContext(uriInfo,httpServletRequest,httpServletResponse,entityType,null);
    }

    protected String getUserIdentifier(HttpServletRequest httpServletRequest)
    {
        return "";
    }

    protected Principal getPrincipal(HttpServletRequest httpServletRequest)
    {
        return null;
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
