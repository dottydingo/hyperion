package com.dottydingo.hyperion.core.endpoint;

import com.dottydingo.hyperion.api.exception.HyperionException;
import com.dottydingo.hyperion.api.exception.InternalException;
import com.dottydingo.hyperion.api.ErrorResponse;
import com.dottydingo.hyperion.core.endpoint.marshall.EndpointMarshaller;
import com.dottydingo.hyperion.core.endpoint.marshall.MarshallingException;
import com.dottydingo.service.endpoint.CompletionCallback;
import com.dottydingo.service.endpoint.context.EndpointResponse;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 */
public class HyperionCompletionCallback implements CompletionCallback<HyperionContext>
{
    private Logger logger = LoggerFactory.getLogger(HyperionCompletionCallback.class);
    private DateTimeFormatter dateFormat = ISODateTimeFormat.basicDateTimeNoMillis();

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

            String message;
            if(error instanceof HyperionException)
            {
                status = ((HyperionException)error).getStatusCode();
                message = ((HyperionException) error).getDetailMessage();
            }
            else
                message = error.getMessage();

            if(status >= 500)
                logger.error(message,cause);
            else
                logger.info(message);


            if(!context.isTimedOut())
            {
                response.setResponseCode(status);

                response.setContentEncoding("UTF-8");
                response.setContentType("application/json");

                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setRequestId(context.getRequestCorrelationId());
                errorResponse.setStatusCode(response.getResponseCode());
                errorResponse.setErrorTime(dateFormat.print(System.currentTimeMillis()));
                errorResponse.setMessage(error.getMessage());
                if (status == 500 || context.getShowErrorDetail())
                    errorResponse.setStackTrace(buildStackTrace(cause));

                String exceptionType = error.getClass().getName();
                if (!(error instanceof HyperionException))
                    exceptionType = InternalException.class.getName();
                else
                    errorResponse.setErrorDetails(((HyperionException) error).getErrorDetails());

                errorResponse.setType(exceptionType);

                try
                {
                    endpointMarshaller.marshall(response.getOutputStream(), errorResponse);
                }
                catch (MarshallingException e)
                {
                    logger.warn("Error masrhalling error response.",e);
                }
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
