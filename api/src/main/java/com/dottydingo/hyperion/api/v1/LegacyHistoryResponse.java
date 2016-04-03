package com.dottydingo.hyperion.api.v1;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.HistoryEntry;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;

/**
 * A history response for V1 clients
 */
@JsonPropertyOrder({"start","responseCount","totalCount","entries"})
public class LegacyHistoryResponse<ID extends Serializable,T extends ApiObject<ID>>
{
    private Integer start;
    private Integer responseCount;
    private Long totalCount;
    private List<HistoryEntry<ID,T>> entries;

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

    /**
     * Return the entries
     * @return THe entries
     */
    public List<HistoryEntry<ID, T>> getEntries()
    {
        return entries;
    }

    /**
     * Set the entries
     * @param entries The entries
     */
    public void setEntries(List<HistoryEntry<ID, T>> entries)
    {
        this.entries = entries;
    }
}
