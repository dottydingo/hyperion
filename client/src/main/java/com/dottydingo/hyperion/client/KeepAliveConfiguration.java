package com.dottydingo.hyperion.client;

/**
 * Client keep alive configuration
 */
public class KeepAliveConfiguration
{
    private int maxIdleConnections;
    private long keepAliveDurationMs;

    /**
     * Get the maximum number of idle connections to maintain
     * @return The maximum idle connections
     */
    public int getMaxIdleConnections()
    {
        return maxIdleConnections;
    }

    /**
     * Set the maximum number of idle connections to maintain
     * @param maxIdleConnections The maximum idle connections
     */
    public void setMaxIdleConnections(int maxIdleConnections)
    {
        this.maxIdleConnections = maxIdleConnections;
    }

    /**
     * Return the keep alive duration in milliseconds
     * @return The keep alive duration
     */
    public long getKeepAliveDurationMs()
    {
        return keepAliveDurationMs;
    }

    /**
     * Set the keep alive duration in milliseconds
     * @param keepAliveDurationMs The keep alive duration
     */
    public void setKeepAliveDurationMs(long keepAliveDurationMs)
    {
        this.keepAliveDurationMs = keepAliveDurationMs;
    }
}
