package com.dottydingo.hyperion.service.configuration;

import com.dottydingo.hyperion.service.persistence.query.DefaultQueryBuilder;
import com.dottydingo.hyperion.service.persistence.query.QueryBuilder;
import com.dottydingo.hyperion.service.persistence.sort.DefaultSortBuilder;
import com.dottydingo.hyperion.service.persistence.sort.SortBuilder;
import cz.jirutka.rsql.parser.model.Comparison;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.criteria.*;
import java.util.*;

/**
 */
public class QueryBuilderFactoryBeanTest
{
    private QueryBuilderFactoryBean factoryBean;

    @Before
    public void setup()
    {
        factoryBean = new QueryBuilderFactoryBean();
        factoryBean.setApiClass(SampleApiObject.class);
    }

    @Test
    public void testStandardMapping() throws Exception
    {
        Map<String,QueryBuilder> map = factoryBean.getObject();

        Assert.assertNotNull(map);
        Assert.assertEquals(7,map.size());

        checkEntry(map,"id");
        checkEntry(map,"created");
        checkEntry(map,"createdBy");
        checkEntry(map,"modified");
        checkEntry(map,"modifiedBy");
        checkEntry(map,"name");
        checkEntry(map,"age");

    }

    @Test
    public void testRemappingMapping() throws Exception
    {
        Map<String,String> remap = new HashMap<String, String>();
        remap.put("modified","updated");
        factoryBean.setFieldNameRemapping(remap);

        Map<String,QueryBuilder> map = factoryBean.getObject();

        Assert.assertNotNull(map);
        Assert.assertEquals(7,map.size());

        checkEntry(map,"id");
        checkEntry(map,"created");
        checkEntry(map,"createdBy");
        checkEntry(map,"updated");
        checkEntry(map,"modifiedBy");
        checkEntry(map,"name");
        checkEntry(map,"age");

    }

    @Test
    public void testOverrideMapping() throws Exception
    {
        Map<String,QueryBuilder> override = new HashMap<String, QueryBuilder>();
        override.put("age",new NoOpQueryBuilder());

        factoryBean.setOverrides(override);

        Map<String,QueryBuilder> map = factoryBean.getObject();

        Assert.assertNotNull(map);
        Assert.assertEquals(7,map.size());

        checkEntry(map,"id");
        checkEntry(map,"created");
        checkEntry(map,"createdBy");
        checkEntry(map,"modified");
        checkEntry(map,"modifiedBy");
        checkEntry(map,"name");
        checkEntry(map,"age",NoOpQueryBuilder.class);

    }

    @Test
    public void testExcludesMapping() throws Exception
    {
        Set<String> excludes = new HashSet<String>();
        excludes.add("created");
        excludes.add("modified");
        factoryBean.setExcludeFields(excludes);

        Map<String,QueryBuilder> map = factoryBean.getObject();

        Assert.assertNotNull(map);
        Assert.assertEquals(5,map.size());

        checkEntry(map,"id");
        checkEntry(map,"createdBy");
        checkEntry(map,"modifiedBy");
        checkEntry(map,"name");
        checkEntry(map,"age");

    }

    private void checkEntry(Map<String,QueryBuilder> map,String fieldName)
    {
        checkEntry(map, fieldName, DefaultQueryBuilder.class);
    }

    private void checkEntry(Map<String,QueryBuilder> map,String fieldName,Class<?> type)
    {
        QueryBuilder builder = map.get(fieldName);
        Assert.assertNotNull(fieldName,builder);
        Assert.assertEquals(type,builder.getClass());
    }

    private class NoOpQueryBuilder implements QueryBuilder
    {
        @Override
        public Predicate buildPredicate(From root, CriteriaBuilder cb, Comparison operator, String argument)
        {
            return null;
        }
    }
}
