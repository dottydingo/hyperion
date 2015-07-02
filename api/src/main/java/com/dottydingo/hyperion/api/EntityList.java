package com.dottydingo.hyperion.api;

import java.util.List;

/**
 */
public class EntityList<T extends ApiObject>
{
    private List<T> entries;

    public List<T> getEntries()
    {
        return entries;
    }

    public void setEntries(List<T> entries)
    {
        this.entries = entries;
    }
}
