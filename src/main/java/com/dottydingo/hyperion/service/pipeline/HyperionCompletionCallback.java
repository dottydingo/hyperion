package com.dottydingo.hyperion.service.pipeline;

import com.dottydingo.hyperion.exception.HyperionException;
import com.dottydingo.hyperion.exception.InternalException;
import com.dottydingo.hyperion.api.ErrorResponse;
import com.dottydingo.hyperion.service.marshall.EndpointMarshaller;
import com.dottydingo.hyperion.service.context.HyperionContext;
import com.dottydingo.service.endpoint.CompletionCallback;
import com.dottydingo.service.endpoint.context.EndpointResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 */
public class HyperionCompletionCallback implements CompletionCallback<HyperionContext>
{
    private Logger logger = LoggerFactory.getLogger(HyperionCompletionCallback.class);

    private EndpointMarshaller endpointMarshaller;

    public void setEndpointMarshaller(EndpointMarshaller endpointMarshaller)
    {
        this.endpointMarshaller = endpointMarshaller;
    }

    @Override
    public void onComplete(HyperionContext context)
    {

        EndpointResponse response = context.getEndpointResponse();

        Throwable error = context.getError();
        if(error != null)
        {
            Throwable cause = getCause(error);

            int status = 500;

            if(error instanceof HyperionException)
            {
                status = ((HyperionException)error).getStatusCode();
            }

            if(status == 500)
                logger.error(cause.getMessage(),cause);
            else
                logger.info(cause.getMessage());


            if(!context.isTimedOut())
            {
                response.setResponseCode(status);

                response.setContentEncoding("UTF-8");
                response.setContentType("application/json");

                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setStatusCode(response.getResponseCode());
                errorResponse.setMessage(error.getMessage());
                if (status == 500 || context.getShowErrorDetail())
                    errorResponse.setStackTrace(buildStackTrace(cause));

                String exceptionType = error.getClass().getName();
                if (!(error instanceof HyperionException))
                    exceptionType = InternalException.class.getName();
                else
                    errorResponse.setErrorDetails(((HyperionException) error).getErrorDetails());

                errorResponse.setType(exceptionType);

                endpointMarshaller.marshall(response.getOutputStream(), errorResponse);
            }
        }

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

    private String buildStackTrace(Throwable t)
    {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        t.printStackTrace(printWriter);
        printWriter.flush();
        return stringWriter.toString();
    }
}
