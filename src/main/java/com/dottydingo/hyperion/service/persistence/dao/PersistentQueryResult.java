package com.dottydingo.hyperion.service.persistence.dao;

import java.util.List;

/**
 * User: mark
 * Date: 2/24/13
 * Time: 8:54 AM
 */
public class PersistentQueryResult<P>
{
    private List<P> results;
    private Long totalCount;

    public List<P> getResults()
    {
        return results;
    }

    public void setResults(List<P> results)
    {
        this.results = results;
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
