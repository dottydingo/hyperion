package com.dottydingo.hyperion.jpa.persistence.sort;

import com.dottydingo.hyperion.core.registry.BaseBuilderFactoryBean;

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
