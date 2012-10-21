package com.dottydingo.hyperion.service.endpoint;

import com.dottydingo.hyperion.exception.HyperionException;
import com.dottydingo.hyperion.exception.InternalException;
import com.dottydingo.hyperion.service.context.RequestContext;
import com.dottydingo.hyperion.service.marshall.EndpointMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;

/**
 */
public class DefaultEndpointExceptionHandler implements EndpointExceptionHandler
{
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Response handleException(Throwable throwable,
                                    EndpointMarshaller marshaller,
                                    RequestContext requestContext)
    {
        Throwable cause = getCause(throwable);

        int status = 500;
        String exceptionType = throwable.getClass().getName();

        if(throwable instanceof HyperionException)
        {
            status = ((HyperionException)throwable).getStatusCode();
        }
        else
        {
            exceptionType = InternalException.class.getName();
        }

        if(status == 500)
            logger.error(cause.getMessage(),cause);
        else
            logger.info(cause.getMessage());

        ErrorResponse response = new ErrorResponse();
        response.setStatusCode(status);
        response.setMessage(cause.getMessage());
        response.setType(exceptionType);
        marshaller.marshall(requestContext.getHttpServletResponse(),response);

        return Response.status(status).build();
    }

    private Throwable getCause(Throwable t)
    {
        Throwable cause = t;
        while (cause.getCause() != null)
        {
            cause = cause.getCause();
        }

        return cause;

    }
}
