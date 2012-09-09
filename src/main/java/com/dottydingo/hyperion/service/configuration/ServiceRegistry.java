package com.dottydingo.hyperion.service.configuration;

import java.util.*;

/**
 */
public class ServiceRegistry
{
    private Map<String,EntityPlugin> endpointMap = new HashMap<String, EntityPlugin>();
    private Map<Class,EntityPlugin> apiClassMap = new HashMap<Class, EntityPlugin>();

    public void setEntityPlugins(List<EntityPlugin> entityPlugins)
    {
        for (EntityPlugin plugin : entityPlugins)
        {
            endpointMap.put(plugin.getEndpointName(),plugin);
            apiClassMap.put(plugin.getApiClass(),plugin);
        }
    }

    public EntityPlugin getPluginForName(String name)
    {
        return endpointMap.get(name);
    }

    public EntityPlugin getPluginForApiClass(Class apiClass)
    {
        return apiClassMap.get(apiClass);
    }

}
