package com.dottydingo.hyperion.core.endpoint.pipeline;

import com.dottydingo.hyperion.api.exception.ServiceUnavailableException;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 */
public class HyperionRejectedExecutionHandler implements RejectedExecutionHandler
{
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor)
    {
        throw new ServiceUnavailableException("No resources available.");
    }
}
