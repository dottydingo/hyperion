package com.dottydingo.hyperion.api;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;

/**
 * A history response
 */
@JsonPropertyOrder({"page","entries"})
public class HistoryResponse<ID extends Serializable,T extends ApiObject<ID>>
{
    private Page page;
    private List<HistoryEntry<ID,T>> entries;

    /**
     * Return the page information
     * @return the page information
     */
    public Page getPage()
    {
        return page;
    }

    /**
     * Set the page information
     * @param page The page information
     */
    public void setPage(Page page)
    {
        this.page = page;
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
