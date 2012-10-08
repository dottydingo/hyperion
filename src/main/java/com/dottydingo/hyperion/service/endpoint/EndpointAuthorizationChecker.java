package com.dottydingo.hyperion.service.endpoint;

import com.dottydingo.hyperion.exception.AuthorizationException;
import com.dottydingo.hyperion.service.context.RequestContext;

/**
 * User: mark
 * Date: 10/7/12
 * Time: 7:08 PM
 */
public interface EndpointAuthorizationChecker
{
    void checkAuthorization(RequestContext requestContext) throws AuthorizationException;
}
