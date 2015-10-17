package com.dottydingo.hyperion.api;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;

/**
 */
@JsonPropertyOrder({"page","entries"})
public class HistoryResponse<ID extends Serializable,T extends ApiObject<ID>>
{
    private Page page;
    private List<HistoryEntry<ID,T>> entries;

    public Page getPage()
    {
        return page;
    }

    public void setPage(Page page)
    {
        this.page = page;
    }

    public List<HistoryEntry<ID, T>> getEntries()
    {
        return entries;
    }

    public void setEntries(List<HistoryEntry<ID, T>> entries)
    {
        this.entries = entries;
    }
}
