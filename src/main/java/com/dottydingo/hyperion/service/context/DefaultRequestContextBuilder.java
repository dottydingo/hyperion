package com.dottydingo.hyperion.service.context;

import javax.ws.rs.core.UriInfo;
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
    public RequestContext buildRequestContext(UriInfo uriInfo, String entityType, String fields)
    {
        RequestContextImpl requestContext = new RequestContextImpl();
        requestContext.setRequestedFields(buildFieldSet(fields));
        requestContext.setEntity(entityType);
        requestContext.setUriInfo(uriInfo);

        requestContext.setAuthorizationContext(buildAuthorizationContext());
        return requestContext;
    }

    protected AuthorizationContext buildAuthorizationContext()
    {
        return new EmptyAuthorizationContext();
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
