package com.dottydingo.hyperion.jpa.configuration;

import com.dottydingo.hyperion.core.configuration.BaseBuilderFactoryBean;
import com.dottydingo.hyperion.jpa.persistence.sort.DefaultJpaEntitySortBuilder;
import com.dottydingo.hyperion.jpa.persistence.sort.JpaEntitySortBuilder;

/**
 */
public class JpaEntitySortBuilderFactoryBean extends BaseBuilderFactoryBean<JpaEntitySortBuilder>
{

    @Override
    protected JpaEntitySortBuilder createBuilder(String fieldName)
    {
        DefaultJpaEntitySortBuilder sortBuilder = new DefaultJpaEntitySortBuilder();
        sortBuilder.setPropertyName(fieldName);
        return sortBuilder;
    }

}
