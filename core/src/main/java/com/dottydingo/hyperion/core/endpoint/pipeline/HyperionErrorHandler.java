package com.dottydingo.hyperion.core.endpoint.pipeline;

import com.dottydingo.hyperion.core.endpoint.HyperionContext;
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
