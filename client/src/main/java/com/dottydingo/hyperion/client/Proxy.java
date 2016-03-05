package com.dottydingo.hyperion.client;

/**
 * Proxy configuration for the client
 */
public class Proxy
{
    private String host;
    private int port;

    /**
     * Create the proxy configuration with the supplied parameters.
     * @param host The proxy host
     * @param port The proxt port
     */
    public Proxy(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    /**
     * Return the proxt host
     * @return The host
     */
    public String getHost()
    {
        return host;
    }

    /**
     * Return the proxy port
     * @return The port
     */
    public int getPort()
    {
        return port;
    }
}
