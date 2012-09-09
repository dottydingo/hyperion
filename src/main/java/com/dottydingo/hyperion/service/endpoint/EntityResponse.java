package com.dottydingo.hyperion.service.endpoint;

import com.dottydingo.hyperion.api.BaseApiObject;

import java.util.List;

/**
 */
public class EntityResponse<T extends BaseApiObject>
{
    private Integer start;
    private Integer pageSize;
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

    public Integer getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
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
