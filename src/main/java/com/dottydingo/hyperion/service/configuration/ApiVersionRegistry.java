package com.dottydingo.hyperion.service.configuration;


import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.service.model.PersistentObject;

import java.util.*;

/**
 */
public class ApiVersionRegistry<C extends ApiObject,P extends PersistentObject>
{
    private Map<Integer,ApiVersionPlugin<C,P>> versionMap = new HashMap<Integer, ApiVersionPlugin<C,P>>();
    private NavigableSet<Integer> versions = new TreeSet<Integer>();

    public void setPlugins(List<ApiVersionPlugin<C,P>> apiVersionPlugins)
    {
        for (ApiVersionPlugin<C,P> plugin : apiVersionPlugins)
        {
            if(versionMap.put(plugin.getVersion(), plugin) != null)
                throw new RuntimeException(String.format(
                        "Duplicate api plugin registered for version %d",plugin.getVersion()));
            versions.add(plugin.getVersion());
        }
    }

    public ApiVersionPlugin<C,P> getPluginForVersion(Integer version)
    {
        Integer effectiveVersion = null;
        if(version == null)
            effectiveVersion = versions.last();
        else
            effectiveVersion = versions.floor(version);

        if (effectiveVersion == null)
        {
            throw new RuntimeException(
                    String.format("Could not resolve an api plugin for version %d",version));

        }

        return versionMap.get(effectiveVersion);
    }
}
