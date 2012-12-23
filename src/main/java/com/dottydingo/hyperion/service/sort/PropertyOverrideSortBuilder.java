package com.dottydingo.hyperion.service.sort;

import org.springframework.data.domain.Sort;

/**
 */
public class PropertyOverrideSortBuilder implements SortBuilder
{
    private String persistentProperty;

    public PropertyOverrideSortBuilder(String persistentProperty)
    {
        this.persistentProperty = persistentProperty;
    }

    @Override
    public Sort buildSort(String field, boolean descending)
    {
        Sort.Direction direction = descending ? Sort.Direction.DESC : Sort.Direction.ASC;
        return new Sort(direction,persistentProperty);
    }
}
