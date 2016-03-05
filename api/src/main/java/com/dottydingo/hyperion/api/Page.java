package com.dottydingo.hyperion.api;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Response paging information
 */
@JsonPropertyOrder({"start","responseCount","totalCount"})
public class Page
{
    private Integer start;
    private Integer responseCount;
    private Long totalCount;

    /**
     * Return the start position
     * @return The start position
     */
    public Integer getStart()
    {
        return start;
    }

    /**
     * Set the start position
     * @param start The start position
     */
    public void setStart(Integer start)
    {
        this.start = start;
    }

    /**
     * Return the response count
     * @return The response count
     */
    public Integer getResponseCount()
    {
        return responseCount;
    }

    /**
     * Set the response count
     * @param responseCount  the response count
     */
    public void setResponseCount(Integer responseCount)
    {
        this.responseCount = responseCount;
    }

    /**
     * Return the total count
     * @return The total count
     */
    public Long getTotalCount()
    {
        return totalCount;
    }

    /**
     * Set the total count
     * @param totalCount the total count
     */
    public void setTotalCount(Long totalCount)
    {
        this.totalCount = totalCount;
    }
}
