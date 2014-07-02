package com.dottydingo.hyperion.core.endpoint;

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
