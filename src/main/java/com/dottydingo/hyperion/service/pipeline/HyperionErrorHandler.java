package com.dottydingo.hyperion.service.pipeline;

import com.dottydingo.hyperion.service.pipeline.context.HyperionContext;
import com.dottydingo.service.endpoint.CompletionHandler;
import com.dottydingo.service.pipeline.ErrorHandler;
import com.dottydingo.service.pipeline.PhaseSelector;

/**
 */
public class HyperionErrorHandler implements ErrorHandler<HyperionContext>
{
    private CompletionHandler<HyperionContext> completionHandler;

    public void setCompletionHandler(CompletionHandler<HyperionContext> completionHandler)
    {
        this.completionHandler = completionHandler;
    }

    @Override
    public void handleError(HyperionContext context, Throwable throwable)
    {

        context.setError(throwable);
        completionHandler.completeRequest(context);
    }

}
