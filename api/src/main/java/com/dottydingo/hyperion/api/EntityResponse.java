package com.dottydingo.hyperion.api;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 */
@JsonPropertyOrder({"start","responseCount","totalCount","entries"})
public class EntityResponse<T extends ApiObject> extends EntityList<T>
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
