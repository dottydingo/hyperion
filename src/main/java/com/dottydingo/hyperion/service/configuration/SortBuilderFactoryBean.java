package com.dottydingo.hyperion.service.configuration;

import com.dottydingo.hyperion.service.persistence.sort.DefaultSortBuilder;
import com.dottydingo.hyperion.service.persistence.sort.SortBuilder;

/**
 */
public class SortBuilderFactoryBean extends BaseBuilderFactoryBean<SortBuilder>
{

    @Override
    protected SortBuilder createBuilder(String fieldName)
    {
        DefaultSortBuilder sortBuilder = new DefaultSortBuilder();
        sortBuilder.setPropertyName(fieldName);
        return sortBuilder;
    }

}
