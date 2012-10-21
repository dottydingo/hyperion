package com.dottydingo.hyperion.service.context;

import com.dottydingo.hyperion.service.configuration.ApiVersionPlugin;
import com.dottydingo.hyperion.service.endpoint.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public RequestContext buildRequestContext(UriInfo uriInfo,
                                              HttpServletRequest httpServletRequest,
                                              HttpServletResponse httpServletResponse,
                                              String entityType,
                                              String fields,
                                              ApiVersionPlugin apiVersionPlugin,
                                              HttpMethod httpMethod)
    {
        RequestContext requestContext = new RequestContext();
        requestContext.setRequestedFields(buildFieldSet(fields));
        requestContext.setEntity(entityType);
        requestContext.setUriInfo(uriInfo);
        requestContext.setHttpServletRequest(httpServletRequest);
        requestContext.setHttpServletResponse(httpServletResponse);
        requestContext.setApiVersionPlugin(apiVersionPlugin);
        requestContext.setHttpMethod(httpMethod);

        requestContext.setUserContext(getUserContext(requestContext));


        return requestContext;
    }


    protected UserContext getUserContext(RequestContext requestContext)
    {
        return new NullUserContext();
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
