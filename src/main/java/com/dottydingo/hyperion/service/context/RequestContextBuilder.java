package com.dottydingo.hyperion.service.context;

import javax.ws.rs.core.UriInfo;

/**
 * User: mark
 * Date: 9/23/12
 * Time: 10:33 AM
 */
public interface RequestContextBuilder
{
    RequestContext buildRequestContext(UriInfo uriInfo, String entityType, String fields);
}
