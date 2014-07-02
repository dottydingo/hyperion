package com.dottydingo.hyperion.core.endpoint.status;

import java.util.concurrent.ThreadPoolExecutor;

/**
 */
public class ExecutorStatus
{

    private final ThreadPoolExecutor executor;

    public ExecutorStatus(ThreadPoolExecutor executor)
    {
        this.executor = executor;
    }

    public void setCorePoolSize(int corePoolSize)
    {
        executor.setCorePoolSize(corePoolSize);
    }

    public int getCorePoolSize()
    {
        return executor.getCorePoolSize();
    }

    public void setMaximumPoolSize(int maximumPoolSize)
    {
        executor.setMaximumPoolSize(maximumPoolSize);
    }

    public int getMaximumPoolSize()
    {
        return executor.getMaximumPoolSize();
    }

    public int getPoolSize()
    {
        return executor.getPoolSize();
    }

    public int getActiveCount()
    {
        return executor.getActiveCount();
    }

    public int getLargestPoolSize()
    {
        return executor.getLargestPoolSize();
    }

    public long getTaskCount()
    {
        return executor.getTaskCount();
    }

    public long getCompletedTaskCount()
    {
        return executor.getCompletedTaskCount();
    }

    public int getQueueSize()
    {
        return executor.getQueue().size();
    }

    public int getQueueRemainingCapacity()
    {
        return executor.getQueue().remainingCapacity();
    }
}
