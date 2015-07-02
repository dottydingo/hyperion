package com.dottydingo.hyperion.client;

/**
 */
public class KeepAliveConfiguration
{
    private int maxIdleConnections;
    private long keepAliveDurationMs;

    public int getMaxIdleConnections()
    {
        return maxIdleConnections;
    }

    public void setMaxIdleConnections(int maxIdleConnections)
    {
        this.maxIdleConnections = maxIdleConnections;
    }

    public long getKeepAliveDurationMs()
    {
        return keepAliveDurationMs;
    }

    public void setKeepAliveDurationMs(long keepAliveDurationMs)
    {
        this.keepAliveDurationMs = keepAliveDurationMs;
    }
}
