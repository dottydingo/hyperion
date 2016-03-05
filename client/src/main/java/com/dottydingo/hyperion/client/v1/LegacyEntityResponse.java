package com.dottydingo.hyperion.client.v1;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.EntityList;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Client for making requests to Hyperion 1.x services. The client is thread safe and a single instance should generally
 * be used to access all endpoints on a service.
 */
@JsonPropertyOrder({"start","responseCount","totalCount","entries"})
public class LegacyEntityResponse<T extends ApiObject> extends EntityList<T>
{
    private Integer start;
    private Integer responseCount;
    private Long totalCount;

    public Integer getStart()
    {
        return start;
    }

    public void setStart(Integer start)
    {
        this.start = start;
    }

    public Integer getResponseCount()
    {
        return responseCount;
    }

    public void setResponseCount(Integer responseCount)
    {
        this.responseCount = responseCount;
    }

    public Long getTotalCount()
    {
        return totalCount;
    }

    public void setTotalCount(Long totalCount)
    {
        this.totalCount = totalCount;
    }

}
