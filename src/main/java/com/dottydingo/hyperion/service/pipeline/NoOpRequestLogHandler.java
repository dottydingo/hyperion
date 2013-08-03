package com.dottydingo.hyperion.service.pipeline;

import com.dottydingo.hyperion.service.context.HyperionContext;
import com.dottydingo.service.endpoint.RequestLogHandler;

/**
 */
public class NoOpRequestLogHandler implements RequestLogHandler<HyperionContext>
{
    @Override
    public void logRequest(HyperionContext context)
    {

    }
}
