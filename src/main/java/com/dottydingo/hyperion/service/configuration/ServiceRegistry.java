package com.dottydingo.hyperion.service.configuration;

import java.util.*;

/**
 */
public class ServiceRegistry
{
    private Map<String,EntityPlugin> endpointMap = new HashMap<String, EntityPlugin>();

    public void setEntityPlugins(List<EntityPlugin> entityPlugins)
    {
        for (EntityPlugin plugin : entityPlugins)
        {
            endpointMap.put(plugin.getEndpointName(),plugin);
        }
    }

    public EntityPlugin getPluginForName(String name)
    {
        return endpointMap.get(name);
    }

}
