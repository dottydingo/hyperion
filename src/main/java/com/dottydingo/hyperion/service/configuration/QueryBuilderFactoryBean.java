package com.dottydingo.hyperion.service.configuration;

import com.dottydingo.hyperion.service.persistence.query.DefaultQueryBuilder;
import com.dottydingo.hyperion.service.persistence.query.QueryBuilder;
import com.dottydingo.hyperion.service.persistence.sort.DefaultSortBuilder;
import com.dottydingo.hyperion.service.persistence.sort.SortBuilder;

/**
 */
public class QueryBuilderFactoryBean extends BaseBuilderFactoryBean<QueryBuilder>
{

    @Override
    protected QueryBuilder createBuilder(String fieldName)
    {
        DefaultQueryBuilder queryBuilder = new DefaultQueryBuilder();
        queryBuilder.setPropertyName(fieldName);
        return queryBuilder;
    }

}
