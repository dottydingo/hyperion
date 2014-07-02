package com.dottydingo.hyperion.core.configuration;

import com.dottydingo.hyperion.core.registry.EntityQueryBuilder;
import com.dottydingo.hyperion.core.registry.EntitySortBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Convenience class for building an API Version plugin with automatically generated sorts and queries.
 *
 * The resulting map of sorts and queries will be resolved as follows: <br>
 * - items defined in the EntityPluginBuilder <br>
 * - items auto generated (using the excludeFields and fieldNameRemapping properties) <br>
 * - items explicitly defined in this bean <br>
 * Items lower in the list will override any items specified higher in the list.
 */
public abstract class AbstractDefaultsVersionPluginBuilder extends ApiVersionPluginBuilder
{
    protected String[] queryExcludeFields = new String[0];
    protected Map<String,String> queryFieldNameRemapping = new HashMap<String, String>();

    protected String[] sortExcludeFields = new String[0];
    protected Map<String,String> sortFieldNameRemapping = new HashMap<String, String>();

    public void setQueryExcludeFields(String[] queryExcludeFields)
    {
        this.queryExcludeFields = queryExcludeFields;
    }

    public void setQueryFieldNameRemapping(Map<String, String> queryFieldNameRemapping)
    {
        this.queryFieldNameRemapping = queryFieldNameRemapping;
    }

    public void setSortExcludeFields(String[] sortExcludeFields)
    {
        this.sortExcludeFields = sortExcludeFields;
    }

    public void setSortFieldNameRemapping(Map<String, String> sortFieldNameRemapping)
    {
        this.sortFieldNameRemapping = sortFieldNameRemapping;
    }

    @Override
    protected Map<String, EntitySortBuilder> getSortBuilder(Map<String, EntitySortBuilder> sortBuilders)
    {
        Map<String,EntitySortBuilder> sorts = new HashMap<>();

        // first add generated
        sorts.putAll(generateSortBuilders());

        // next add specified so they can override generated
        sorts.putAll(sortBuilders);

        return sorts;
    }

    @Override
    protected Map<String, EntityQueryBuilder> getQueryBuilders(Map<String, EntityQueryBuilder> queryBuilders)
    {
        Map<String, EntityQueryBuilder> queries = new HashMap<>();

        // first add generated
        queries.putAll(generateQueryBuilders());

        // next add specified so they can override generated
        queries.putAll(queryBuilders);

        return queries;
    }

    protected abstract Map<String, ? extends EntityQueryBuilder> generateQueryBuilders();

    protected abstract Map<String, ? extends EntitySortBuilder> generateSortBuilders();

}
