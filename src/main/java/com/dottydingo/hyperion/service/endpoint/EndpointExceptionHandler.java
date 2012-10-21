package com.dottydingo.hyperion.service.endpoint;

import com.dottydingo.hyperion.service.context.RequestContext;
import com.dottydingo.hyperion.service.marshall.EndpointMarshaller;

import javax.ws.rs.core.Response;

/**
 */
public interface EndpointExceptionHandler
{
    Response handleException(Throwable throwable, EndpointMarshaller marshaller,
                             RequestContext requestContext);
}
