package com.dottydingo.hyperion.core.configuration;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.core.persistence.CreateKeyProcessor;
import com.dottydingo.hyperion.core.registry.EntityQueryBuilder;
import com.dottydingo.hyperion.core.registry.EntitySortBuilder;
import com.dottydingo.hyperion.core.translation.Translator;
import com.dottydingo.hyperion.core.validation.Validator;

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

    public AbstractDefaultsVersionPluginBuilder setQueryExcludeFields(String... queryExcludeFields)
    {
        this.queryExcludeFields = queryExcludeFields;
        return this;
    }

    public void setQueryFieldNameRemapping(Map<String, String> queryFieldNameRemapping)
    {
        this.queryFieldNameRemapping = queryFieldNameRemapping;
    }

    public AbstractDefaultsVersionPluginBuilder addQueryFieldRemapping(String propertyName,String mappedPropertyName)
    {
        queryFieldNameRemapping.put(propertyName,mappedPropertyName);
        return this;
    }

    public AbstractDefaultsVersionPluginBuilder setSortExcludeFields(String... sortExcludeFields)
    {
        this.sortExcludeFields = sortExcludeFields;
        return this;
    }

    public void setSortFieldNameRemapping(Map<String, String> sortFieldNameRemapping)
    {
        this.sortFieldNameRemapping = sortFieldNameRemapping;
    }

    public AbstractDefaultsVersionPluginBuilder addSortFieldRemapping(String propertyName,String mappedPropertyName)
    {
        sortFieldNameRemapping.put(propertyName, mappedPropertyName);
        return this;
    }

    @Override
    public AbstractDefaultsVersionPluginBuilder setApiClass(Class<? extends ApiObject> apiClass)
    {
        super.setApiClass(apiClass);
        return this;
    }

    @Override
    public AbstractDefaultsVersionPluginBuilder setTranslator(Translator translator)
    {
        super.setTranslator(translator);
        return this;
    }

    @Override
    public AbstractDefaultsVersionPluginBuilder setValidator(Validator validator)
    {
        super.setValidator(validator);
        return this;
    }

    @Override
    public AbstractDefaultsVersionPluginBuilder addSortBuilder(String name, EntitySortBuilder builder)
    {
        super.addSortBuilder(name, builder);
        return this;
    }

    @Override
    public AbstractDefaultsVersionPluginBuilder addQueryBuilder(String name, EntityQueryBuilder builder)
    {
        super.addQueryBuilder(name, builder);
        return this;
    }

    @Override
    public AbstractDefaultsVersionPluginBuilder setCreateKeyProcessor(CreateKeyProcessor createKeyProcessor)
    {
        super.setCreateKeyProcessor(createKeyProcessor);
        return this;
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
