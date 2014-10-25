package com.dottydingo.hyperion.core.registry;


import com.dottydingo.hyperion.core.Util;
import com.dottydingo.hyperion.core.endpoint.HttpMethod;
import com.dottydingo.hyperion.core.persistence.event.EntityChangeEvent;
import com.dottydingo.hyperion.core.persistence.event.EntityChangeListener;
import com.dottydingo.hyperion.core.persistence.event.PersistentChangeEvent;
import com.dottydingo.hyperion.core.persistence.event.PersistentChangeListener;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;

public class EntityPluginTest
{
    private EntityPlugin entityPlugin;

    @Before
    public void setup()
    {
        entityPlugin = new EntityPlugin();
    }

    @Test
    public void testIsMethodAllowed() throws Exception
    {
        Assert.assertTrue(entityPlugin.isMethodAllowed(HttpMethod.GET));

        entityPlugin.setLimitMethods(Util.asSet(HttpMethod.GET, HttpMethod.DELETE));

        Assert.assertTrue(entityPlugin.isMethodAllowed(HttpMethod.GET));
        Assert.assertFalse(entityPlugin.isMethodAllowed(HttpMethod.POST));

        // this should always be allowed
        Assert.assertTrue(entityPlugin.isMethodAllowed(HttpMethod.OPTIONS));
    }

    @Test
    public void testFilterAllowedMethods() throws Exception
    {
        Set<HttpMethod> methods = Util.asSet(HttpMethod.GET,HttpMethod.POST);
        entityPlugin.filterAllowedMethods(methods);

        Assert.assertEquals(Util.asSet(HttpMethod.GET,HttpMethod.POST),methods);

        entityPlugin.setLimitMethods(Util.asSet(HttpMethod.GET));

        methods = Util.asSet(HttpMethod.GET,HttpMethod.POST);
        entityPlugin.filterAllowedMethods(methods);

        Assert.assertEquals(Util.asSet(HttpMethod.GET),methods);
    }

    @Test
    public void testHasEntityChangeListeners() throws Exception
    {
        Assert.assertFalse(entityPlugin.hasEntityChangeListeners());

        entityPlugin.setEntityChangeListeners(Collections.singletonList(new EntityChangeListener()
        {
            @Override
            public void processEntityChange(EntityChangeEvent event)
            {

            }
        }));

        Assert.assertTrue(entityPlugin.hasEntityChangeListeners());
    }

    @Test
    public void testHasPersistentChangeListeners() throws Exception
    {
        Assert.assertFalse(entityPlugin.hasPersistentChangeListeners());

        entityPlugin.setPersistentChangeListeners(Collections.singletonList(new PersistentChangeListener()
        {
            @Override
            public void processEntityChange(PersistentChangeEvent event)
            {

            }
        }));

        Assert.assertTrue(entityPlugin.hasPersistentChangeListeners());
    }

    @Test
    public void testHasListeners() throws Exception
    {
        Assert.assertFalse(entityPlugin.hasListeners());

        entityPlugin.setEntityChangeListeners(Collections.singletonList(new EntityChangeListener()
        {
            @Override
            public void processEntityChange(EntityChangeEvent event)
            {

            }
        }));

        Assert.assertTrue(entityPlugin.hasListeners());

        entityPlugin.setEntityChangeListeners(Collections.emptyList());

        Assert.assertFalse(entityPlugin.hasListeners());

        entityPlugin.setPersistentChangeListeners(Collections.singletonList(new PersistentChangeListener()
        {
            @Override
            public void processEntityChange(PersistentChangeEvent event)
            {

            }
        }));

        Assert.assertTrue(entityPlugin.hasListeners());
        entityPlugin.setEntityChangeListeners(Collections.singletonList(new EntityChangeListener()
        {
            @Override
            public void processEntityChange(EntityChangeEvent event)
            {

            }
        }));

        Assert.assertTrue(entityPlugin.hasListeners());
    }

    @Test
    public void testIsReserved()
    {
        Assert.assertTrue(entityPlugin.isReserved("start"));
        Assert.assertTrue(entityPlugin.isReserved("limit"));
        Assert.assertTrue(entityPlugin.isReserved("fields"));
        Assert.assertTrue(entityPlugin.isReserved("sort"));
        Assert.assertTrue(entityPlugin.isReserved("query"));
        Assert.assertTrue(entityPlugin.isReserved("trace"));
        Assert.assertTrue(entityPlugin.isReserved("version"));
        Assert.assertTrue(entityPlugin.isReserved("cid"));

        Assert.assertFalse(entityPlugin.isReserved("foobar"));
    }
}