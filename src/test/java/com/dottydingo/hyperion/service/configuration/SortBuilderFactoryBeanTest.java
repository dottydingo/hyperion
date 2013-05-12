package com.dottydingo.hyperion.service.configuration;

import com.dottydingo.hyperion.service.persistence.sort.DefaultSortBuilder;
import com.dottydingo.hyperion.service.persistence.sort.SortBuilder;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 */
public class SortBuilderFactoryBeanTest
{
    private SortBuilderFactoryBean sortBuilderFactoryBean;

    @Before
    public void setup()
    {
        sortBuilderFactoryBean = new SortBuilderFactoryBean();
        sortBuilderFactoryBean.setApiClass(SampleApiObject.class);
    }

    @Test
    public void testStandardMapping() throws Exception
    {
        Map<String,SortBuilder> map = sortBuilderFactoryBean.getObject();

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

        Map<String,SortBuilder> map = sortBuilderFactoryBean.getObject();

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
        Map<String,SortBuilder> override = new HashMap<String, SortBuilder>();
        override.put("age",new NoOpSortBuilder());

        sortBuilderFactoryBean.setOverrides(override);

        Map<String,SortBuilder> map = sortBuilderFactoryBean.getObject();

        Assert.assertNotNull(map);
        Assert.assertEquals(7,map.size());

        checkEntry(map,"id");
        checkEntry(map,"created");
        checkEntry(map,"createdBy");
        checkEntry(map,"modified");
        checkEntry(map,"modifiedBy");
        checkEntry(map,"name");
        checkEntry(map,"age",NoOpSortBuilder.class);

    }

    @Test
    public void testExcludesMapping() throws Exception
    {
        Set<String> excludes = new HashSet<String>();
        excludes.add("created");
        excludes.add("modified");
        sortBuilderFactoryBean.setExcludeFields(excludes);

        Map<String,SortBuilder> map = sortBuilderFactoryBean.getObject();

        Assert.assertNotNull(map);
        Assert.assertEquals(5,map.size());

        checkEntry(map,"id");
        checkEntry(map,"createdBy");
        checkEntry(map,"modifiedBy");
        checkEntry(map,"name");
        checkEntry(map,"age");

    }

    private void checkEntry(Map<String,SortBuilder> map,String fieldName)
    {
        checkEntry(map, fieldName, DefaultSortBuilder.class);
    }

    private void checkEntry(Map<String,SortBuilder> map,String fieldName,Class<?> type)
    {
        SortBuilder builder = map.get(fieldName);
        Assert.assertNotNull(fieldName,builder);
        Assert.assertEquals(type,builder.getClass());
    }

    private class NoOpSortBuilder implements SortBuilder
    {
        @Override
        public List<Order> buildOrder(boolean desc, CriteriaBuilder cb, Root root)
        {
            return null;
        }
    }
}
