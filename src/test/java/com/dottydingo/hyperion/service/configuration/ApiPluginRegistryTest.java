package com.dottydingo.hyperion.service.configuration;

import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class ApiPluginRegistryTest
{
    @Test
    public void testGetPluginForVersion()
    {
        List<ApiVersionPlugin> plugins = new ArrayList<ApiVersionPlugin>();
        plugins.add(new ApiVersionPlugin(1,null,null));
        plugins.add(new ApiVersionPlugin(5,null,null));
        plugins.add(new ApiVersionPlugin(10,null,null));

        ApiVersionRegistry registry = new ApiVersionRegistry();
        registry.setPlugins(plugins);

        ApiVersionPlugin t = registry.getPluginForVersion(1);
        Assert.assertNotNull(t);
        Assert.assertEquals(new Integer(1),t.getVersion());

        t = registry.getPluginForVersion(4);
        Assert.assertNotNull(t);
        Assert.assertEquals(new Integer(1),t.getVersion());

        t = registry.getPluginForVersion(5);
        Assert.assertNotNull(t);
        Assert.assertEquals(new Integer(5),t.getVersion());

        t = registry.getPluginForVersion(11);
        Assert.assertNotNull(t);
        Assert.assertEquals(new Integer(10),t.getVersion());

        t = registry.getPluginForVersion(null);
        Assert.assertNotNull(t);
        Assert.assertEquals(new Integer(10),t.getVersion());
    }


}
