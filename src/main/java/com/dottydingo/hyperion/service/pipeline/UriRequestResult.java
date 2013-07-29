package com.dottydingo.hyperion.service.pipeline;

/**
 */
public class UriRequestResult
{
    private String endpoint;
    private boolean history;
    private String id;

    public String getEndpoint()
    {
        return endpoint;
    }

    public void setEndpoint(String endpoint)
    {
        this.endpoint = endpoint;
    }

    public boolean isHistory()
    {
        return history;
    }

    public void setHistory(boolean history)
    {
        this.history = history;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
