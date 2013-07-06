package com.dottydingo.hyperion.service.pipeline;

/**
 */
public class UriRequestResult
{
    private String endpoint;
    private boolean audit;
    private String id;

    public String getEndpoint()
    {
        return endpoint;
    }

    public void setEndpoint(String endpoint)
    {
        this.endpoint = endpoint;
    }

    public boolean isAudit()
    {
        return audit;
    }

    public void setAudit(boolean audit)
    {
        this.audit = audit;
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
