package com.dottydingo.hyperion.service.endpoint;

import com.dottydingo.hyperion.exception.InternalException;
import com.dottydingo.hyperion.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * User: mark
 * Date: 9/18/12
 * Time: 9:26 PM
 */
@Provider
public class ServiceExceptionMapper implements ExceptionMapper<Throwable>
{
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Response toResponse(Throwable exception)
    {
        Throwable cause = getCause(exception);

        int status = 500;
        String exceptionType = exception.getClass().getName();

        if(exception instanceof ServiceException)
        {
            status = ((ServiceException)exception).getStatusCode();
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
        response.setMessage(exception.getMessage());
        response.setType(exceptionType);
        return Response.status(status).entity(response).build();
    }

    private Throwable getCause(Throwable t)
    {
        Throwable cause = t;
        while(cause.getCause() != null)
            cause = cause.getCause();

        return cause;

    }
}
