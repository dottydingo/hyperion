package com.dottydingo.hyperion.service.endpoint;

import com.dottydingo.hyperion.api.ApiObject;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 */
@JsonPropertyOrder({"start","responseCount","totalCount","entries"})
public class EntityResponse<T extends ApiObject>
{
    private Integer start;
    private Integer responseCount;
    private Long totalCount;
    private List<T> entries;

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

    public List<T> getEntries()
    {
        return entries;
    }

    public void setEntries(List<T> entries)
    {
        this.entries = entries;
    }
}
