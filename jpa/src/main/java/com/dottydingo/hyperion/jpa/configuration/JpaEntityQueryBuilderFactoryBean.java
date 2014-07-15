package com.dottydingo.hyperion.jpa.configuration;

import com.dottydingo.hyperion.core.configuration.BaseBuilderFactoryBean;
import com.dottydingo.hyperion.jpa.persistence.query.DefaultJpaEntityQueryBuilder;
import com.dottydingo.hyperion.jpa.persistence.query.JpaEntityQueryBuilder;

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
