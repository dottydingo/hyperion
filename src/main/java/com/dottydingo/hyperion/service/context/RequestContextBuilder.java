package com.dottydingo.hyperion.service.context;

import com.dottydingo.hyperion.service.configuration.ApiVersionPlugin;
import com.dottydingo.hyperion.service.endpoint.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.UriInfo;

/**
 * User: mark
 * Date: 9/23/12
 * Time: 10:33 AM
 */
public interface RequestContextBuilder
{
    RequestContext buildRequestContext(UriInfo uriInfo,
                                       HttpServletRequest httpServletRequest,
                                       HttpServletResponse httpServletResponse,
                                       String entityType, String fields, ApiVersionPlugin apiVersionPlugin,
                                       HttpMethod httpMethod);

}

