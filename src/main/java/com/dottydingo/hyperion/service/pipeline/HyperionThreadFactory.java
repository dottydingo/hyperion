package com.dottydingo.hyperion.service.pipeline;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 */
public class HyperionThreadFactory implements ThreadFactory
{
    private final AtomicInteger threadCounter = new AtomicInteger(1);
    private String threadNamePrefix = "hyperionThread";

    public void setThreadNamePrefix(String threadNamePrefix)
    {
        this.threadNamePrefix = threadNamePrefix;
    }

    @Override
    public Thread newThread(Runnable r)
    {
        return new Thread(r,threadNamePrefix + "-" + threadCounter.getAndIncrement());
    }
}
