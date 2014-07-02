package com.dottydingo.hyperion.client.event;

import com.dottydingo.hyperion.client.RequestMethod;

import java.io.Serializable;

/**
 */
public class ClientEvent implements Serializable
{
    private static final long serialVersionUID = 6888151339603349427L;

    private String host;
    private String entity;
    private RequestMethod requestMethod;
    private long duration;
    private boolean error;

    public ClientEvent(String host, String entity, RequestMethod requestMethod, long duration, boolean error)
    {
        this.host = host;
        this.entity = entity;
        this.requestMethod = requestMethod;
        this.duration = duration;
        this.error = error;
    }

    public static long getSerialVersionUID()
    {
        return serialVersionUID;
    }

    public String getHost()
    {
        return host;
    }

    public String getEntity()
    {
        return entity;
    }

    public RequestMethod getRequestMethod()
    {
        return requestMethod;
    }

    public long getDuration()
    {
        return duration;
    }

    public boolean isError()
    {
        return error;
    }
}
