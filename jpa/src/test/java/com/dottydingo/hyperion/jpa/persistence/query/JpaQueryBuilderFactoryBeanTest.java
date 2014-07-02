package com.dottydingo.hyperion.jpa.persistence.query;

import com.dottydingo.hyperion.jpa.persistence.SampleApiObject;
import cz.jirutka.rsql.parser.model.Comparison;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.criteria.*;
import java.util.*;

/**
 */
public class JpaQueryBuilderFactoryBeanTest
{
    private JpaEntityQueryBuilderFactoryBean factoryBean;

    @Before
    public void setup()
    {
        factoryBean = new JpaEntityQueryBuilderFactoryBean();
        factoryBean.setApiClass(SampleApiObject.class);
    }

    @Test
    public void testStandardMapping() throws Exception
    {
        Map<String,JpaEntityQueryBuilder> map = factoryBean.getObject();

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

        Map<String,JpaEntityQueryBuilder> map = factoryBean.getObject();

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
        Map<String,JpaEntityQueryBuilder> override = new HashMap<String, JpaEntityQueryBuilder>();
        override.put("age",new NoOpJpaQueryBuilder());

        factoryBean.setOverrides(override);

        Map<String,JpaEntityQueryBuilder> map = factoryBean.getObject();

        Assert.assertNotNull(map);
        Assert.assertEquals(7,map.size());

        checkEntry(map,"id");
        checkEntry(map,"created");
        checkEntry(map,"createdBy");
        checkEntry(map,"modified");
        checkEntry(map,"modifiedBy");
        checkEntry(map,"name");
        checkEntry(map,"age",NoOpJpaQueryBuilder.class);

    }

    @Test
    public void testExcludesMapping() throws Exception
    {
        Set<String> excludes = new HashSet<String>();
        excludes.add("created");
        excludes.add("modified");
        factoryBean.setExcludeFields(excludes);

        Map<String,JpaEntityQueryBuilder> map = factoryBean.getObject();

        Assert.assertNotNull(map);
        Assert.assertEquals(5,map.size());

        checkEntry(map,"id");
        checkEntry(map,"createdBy");
        checkEntry(map,"modifiedBy");
        checkEntry(map,"name");
        checkEntry(map,"age");

    }

    private void checkEntry(Map<String,JpaEntityQueryBuilder> map,String fieldName)
    {
        checkEntry(map, fieldName, DefaultJpaEntityQueryBuilder.class);
    }

    private void checkEntry(Map<String,JpaEntityQueryBuilder> map,String fieldName,Class<?> type)
    {
        JpaEntityQueryBuilder builder = map.get(fieldName);
        Assert.assertNotNull(fieldName,builder);
        Assert.assertEquals(type,builder.getClass());
    }

    private class NoOpJpaQueryBuilder implements JpaEntityQueryBuilder
    {
        @Override
        public Predicate buildPredicate(From root, CriteriaBuilder cb, Comparison operator, String argument)
        {
            return null;
        }
    }
}
