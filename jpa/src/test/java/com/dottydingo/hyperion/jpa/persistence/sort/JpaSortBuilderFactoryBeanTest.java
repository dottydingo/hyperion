package com.dottydingo.hyperion.jpa.persistence.sort;

import com.dottydingo.hyperion.jpa.configuration.JpaEntitySortBuilderFactoryBean;
import com.dottydingo.hyperion.jpa.persistence.SampleApiObject;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Order;
import java.util.*;

/**
 */
public class JpaSortBuilderFactoryBeanTest
{
    private JpaEntitySortBuilderFactoryBean sortBuilderFactoryBean;

    @Before
    public void setup()
    {
        sortBuilderFactoryBean = new JpaEntitySortBuilderFactoryBean();
        sortBuilderFactoryBean.setApiClass(SampleApiObject.class);
    }

    @Test
    public void testStandardMapping() throws Exception
    {
        Map<String,JpaEntitySortBuilder> map = sortBuilderFactoryBean.getObject();

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
        sortBuilderFactoryBean.setFieldNameRemapping(remap);

        Map<String,JpaEntitySortBuilder> map = sortBuilderFactoryBean.getObject();

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
        Map<String,JpaEntitySortBuilder> override = new HashMap<String, JpaEntitySortBuilder>();
        override.put("age",new NoOpJpaSortBuilder());

        sortBuilderFactoryBean.setOverrides(override);

        Map<String,JpaEntitySortBuilder> map = sortBuilderFactoryBean.getObject();

        Assert.assertNotNull(map);
        Assert.assertEquals(7,map.size());

        checkEntry(map,"id");
        checkEntry(map,"created");
        checkEntry(map,"createdBy");
        checkEntry(map,"modified");
        checkEntry(map,"modifiedBy");
        checkEntry(map,"name");
        checkEntry(map,"age",NoOpJpaSortBuilder.class);

    }

    @Test
    public void testExcludesMapping() throws Exception
    {
        Set<String> excludes = new HashSet<String>();
        excludes.add("created");
        excludes.add("modified");
        sortBuilderFactoryBean.setExcludeFields(excludes);

        Map<String,JpaEntitySortBuilder> map = sortBuilderFactoryBean.getObject();

        Assert.assertNotNull(map);
        Assert.assertEquals(5,map.size());

        checkEntry(map,"id");
        checkEntry(map,"createdBy");
        checkEntry(map,"modifiedBy");
        checkEntry(map,"name");
        checkEntry(map,"age");

    }

    private void checkEntry(Map<String,JpaEntitySortBuilder> map,String fieldName)
    {
        checkEntry(map, fieldName, DefaultJpaEntitySortBuilder.class);
    }

    private void checkEntry(Map<String,JpaEntitySortBuilder> map,String fieldName,Class<?> type)
    {
        JpaEntitySortBuilder builder = map.get(fieldName);
        Assert.assertNotNull(fieldName,builder);
        Assert.assertEquals(type,builder.getClass());
    }

    private class NoOpJpaSortBuilder implements JpaEntitySortBuilder
    {
        @Override
        public List<Order> buildOrder(boolean desc, CriteriaBuilder cb, From root)
        {
            return null;
        }
    }
}
