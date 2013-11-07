package com.dottydingo.hyperion.service.configuration;

import java.util.*;

/**
 */
public class ServiceRegistry
{
    private Map<String,EntityPlugin> entityPluginMap = new TreeMap<String, EntityPlugin>();

    public void setEntityPlugins(List<EntityPlugin> entityPlugins)
    {
        for (EntityPlugin plugin : entityPlugins)
        {
            this.entityPluginMap.put(plugin.getEndpointName(), plugin);
        }
    }

    public List<EntityPlugin> getEntityPlugins()
    {
        return new ArrayList<EntityPlugin>(entityPluginMap.values());
    }

    public EntityPlugin getPluginForName(String name)
    {
        return entityPluginMap.get(name);
    }

}
