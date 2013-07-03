package com.dottydingo.hyperion.service.pipeline;

import com.dottydingo.hyperion.exception.HyperionException;
import com.dottydingo.hyperion.exception.InternalException;
import com.dottydingo.hyperion.service.endpoint.ErrorResponse;
import com.dottydingo.hyperion.service.marshall.EndpointMarshaller;
import com.dottydingo.hyperion.service.pipeline.context.HyperionContext;
import com.dottydingo.service.pipeline.Phase;
import com.dottydingo.service.endpoint.context.EndpointResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class CompleteRequestPhase implements Phase<HyperionContext>
{
    private Logger logger = LoggerFactory.getLogger(CompleteRequestPhase.class);

    private EndpointMarshaller endpointMarshaller;

    public void setEndpointMarshaller(EndpointMarshaller endpointMarshaller)
    {
        this.endpointMarshaller = endpointMarshaller;
    }

    @Override
    public void execute(HyperionContext phaseContext) throws Exception
    {
        logger.debug("Starting CompleteRequestPhase");

        Throwable error = phaseContext.getError();
        if(error != null)
        {
            EndpointResponse response = phaseContext.getEndpointResponse();
            response.setContentEncoding("UTF-8");
            response.setContentType("application/json");

            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setStatusCode(response.getResponseCode());
            errorResponse.setMessage(error.getMessage());

            String exceptionType = error.getClass().getName();
            if(!(error instanceof HyperionException))
                exceptionType = InternalException.class.getName();

            errorResponse.setType(exceptionType);

            endpointMarshaller.marshall(response.getOutputStream(),errorResponse);
        }

        logger.debug("Ending CompleteRequestPhase");
    }
}
