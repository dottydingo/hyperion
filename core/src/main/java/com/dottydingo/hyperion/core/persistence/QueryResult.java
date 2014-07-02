package com.dottydingo.hyperion.core.persistence;

import java.util.List;

/**
 */
public class QueryResult<T>
{
    private List<T> items;
    private int start;
    private int responseCount;
    private long totalCount;

    public List<T> getItems()
    {
        return items;
    }

    public void setItems(List<T> items)
    {
        this.items = items;
    }

    public int getStart()
    {
        return start;
    }

    public void setStart(int start)
    {
        this.start = start;
    }

    public int getResponseCount()
    {
        return responseCount;
    }

    public void setResponseCount(int responseCount)
    {
        this.responseCount = responseCount;
    }

    public long getTotalCount()
    {
        return totalCount;
    }

    public void setTotalCount(long totalCount)
    {
        this.totalCount = totalCount;
    }
}
