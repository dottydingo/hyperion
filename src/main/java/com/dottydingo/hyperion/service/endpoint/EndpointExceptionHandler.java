package com.dottydingo.hyperion.service.endpoint;

import com.dottydingo.hyperion.service.context.RequestContext;
import com.dottydingo.hyperion.service.marshall.EndpointMarshaller;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

/**
 */
public interface EndpointExceptionHandler
{
    void handleException(Throwable throwable, EndpointMarshaller marshaller,
                            HttpServletResponse httpServletResponse);

}
