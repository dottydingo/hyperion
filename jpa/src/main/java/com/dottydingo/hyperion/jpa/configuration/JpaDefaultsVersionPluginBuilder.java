package com.dottydingo.hyperion.jpa.configuration;

import com.dottydingo.hyperion.core.configuration.AbstractDefaultsVersionPluginBuilder;
import com.dottydingo.hyperion.core.registry.EntityQueryBuilder;
import com.dottydingo.hyperion.core.registry.EntitySortBuilder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

/**
 */
public class JpaDefaultsVersionPluginBuilder extends AbstractDefaultsVersionPluginBuilder
{
    @Override
    protected Map<String, ? extends EntityQueryBuilder> generateQueryBuilders()
    {
        JpaEntityQueryBuilderFactoryBean factoryBean = new JpaEntityQueryBuilderFactoryBean();
        if(queryExcludeFields != null && queryExcludeFields.length >0)
            factoryBean.setExcludeFields(new HashSet<String>(Arrays.asList(queryExcludeFields)));

        factoryBean.setFieldNameRemapping(queryFieldNameRemapping);
        factoryBean.setApiClass(apiClass);

        try
        {
            return factoryBean.getObject();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error generating queries",e);
        }
    }

    @Override
    protected Map<String, ? extends EntitySortBuilder> generateSortBuilders()
    {
        JpaEntitySortBuilderFactoryBean factoryBean = new JpaEntitySortBuilderFactoryBean();
        if(sortExcludeFields != null && sortExcludeFields.length > 0)
            factoryBean.setExcludeFields(new HashSet<String>(Arrays.asList(sortExcludeFields)));

        factoryBean.setFieldNameRemapping(sortFieldNameRemapping);
        factoryBean.setApiClass(apiClass);

        try
        {
            return factoryBean.getObject();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error generating sorts",e);
        }
    }
}
