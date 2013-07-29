package com.dottydingo.hyperion.service.endpoint;

import com.dottydingo.hyperion.api.ApiObject;

import java.io.Serializable;
import java.util.List;

/**
 */
public class HistoryResponse<ID extends Serializable,T extends ApiObject<ID>>
{
    private List<HistoryEntry<ID,T>> entries;

    public List<HistoryEntry<ID, T>> getEntries()
    {
        return entries;
    }

    public void setEntries(List<HistoryEntry<ID, T>> entries)
    {
        this.entries = entries;
    }
}
