package com.dottydingo.hyperion.service.context;

import com.dottydingo.hyperion.service.endpoint.HttpMethod;

import javax.ws.rs.core.UriInfo;

/**
 * User: mark
 * Date: 9/23/12
 * Time: 10:33 AM
 */
public interface RequestContextBuilder
{
    RequestContext buildRequestContext(String entityType, HttpMethod httpMethod, String fields);
    RequestContext buildRequestContext(String entityType, HttpMethod httpMethod);
}

