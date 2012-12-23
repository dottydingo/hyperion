package com.dottydingo.hyperion.service.sort;

import org.springframework.data.domain.Sort;

/**
 */
public class DefaultSortBuilder implements SortBuilder
{
    @Override
    public Sort buildSort(String field, boolean descending)
    {
        Sort.Direction direction = descending ? Sort.Direction.DESC : Sort.Direction.ASC;
        return new Sort(direction,field);
    }
}
