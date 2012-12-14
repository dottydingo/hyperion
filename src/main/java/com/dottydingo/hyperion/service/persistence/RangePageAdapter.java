package com.dottydingo.hyperion.service.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 */
public class RangePageAdapter implements Pageable
{
    private int offset;
    private int size;
    private Sort sort;

    public RangePageAdapter(int start, int size, Sort sort)
    {
        this.offset = start - 1;
        this.size = size;
        this.sort = sort;
    }

    @Override
    public int getPageNumber()
    {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public int getPageSize()
    {
        return size;
    }

    @Override
    public int getOffset()
    {
        return offset;
    }

    @Override
    public Sort getSort()
    {
        return sort;
    }
}
