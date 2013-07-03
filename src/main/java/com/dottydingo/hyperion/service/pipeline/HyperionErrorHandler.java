package com.dottydingo.hyperion.service.pipeline;

import com.dottydingo.hyperion.exception.HyperionException;
import com.dottydingo.hyperion.service.pipeline.context.HyperionContext;
import com.dottydingo.service.pipeline.ErrorHandler;
import com.dottydingo.service.pipeline.PhaseSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class HyperionErrorHandler implements ErrorHandler<HyperionContext>
{
    private Logger logger = LoggerFactory.getLogger(getClass());
    private PhaseSelector<HyperionContext> nextPhaseSelector;

    public void setNextPhaseSelector(PhaseSelector<HyperionContext> nextPhaseSelector)
    {
        this.nextPhaseSelector = nextPhaseSelector;
    }

    @Override
    public void handleError(HyperionContext context, Throwable throwable)
    {
        Throwable cause = getCause(throwable);

        int status = 500;

        if(throwable instanceof HyperionException)
        {
            status = ((HyperionException)throwable).getStatusCode();
        }

        if(status == 500)
            logger.error(cause.getMessage(),cause);
        else
            logger.info(cause.getMessage());

        context.getEndpointResponse().setResponseCode(status);
        context.setError(cause);

        nextPhaseSelector.getNextPhase(context).execute(context);
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
