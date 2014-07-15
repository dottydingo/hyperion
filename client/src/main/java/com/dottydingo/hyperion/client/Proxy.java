package com.dottydingo.hyperion.client;

/**
 */
public class Proxy
{
    private String host;
    private int port;

    public Proxy(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    public String getHost()
    {
        return host;
    }

    public int getPort()
    {
        return port;
    }
}
