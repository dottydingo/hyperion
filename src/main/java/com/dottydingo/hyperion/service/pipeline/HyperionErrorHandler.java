package com.dottydingo.hyperion.service.pipeline;

import com.dottydingo.hyperion.service.context.HyperionContext;
import com.dottydingo.service.endpoint.CompletionHandler;
import com.dottydingo.service.pipeline.ErrorHandler;

/**
 */
public class HyperionErrorHandler implements ErrorHandler<HyperionContext>
{

    @Override
    public void handleError(HyperionContext context, Throwable throwable)
    {

        context.setError(throwable);
        context.requestComplete();
    }

}
