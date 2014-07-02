package com.dottydingo.hyperion.jpa.persistence.query;

import com.dottydingo.hyperion.core.configuration.BaseBuilderFactoryBean;

/**
 */
public class JpaEntityQueryBuilderFactoryBean extends BaseBuilderFactoryBean<JpaEntityQueryBuilder>
{

    @Override
    protected JpaEntityQueryBuilder createBuilder(String fieldName)
    {
        DefaultJpaEntityQueryBuilder queryBuilder = new DefaultJpaEntityQueryBuilder();
        queryBuilder.setPropertyName(fieldName);
        return queryBuilder;
    }

}
