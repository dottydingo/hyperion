package com.dottydingo.hyperion.core.registry;


import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.core.model.PersistentObject;

import java.io.Serializable;
import java.util.*;

/**
 */
public class ApiVersionRegistry<C extends ApiObject<ID>, P extends PersistentObject<ID>, ID extends Serializable>
{
    private Map<Integer,ApiVersionPlugin<C,P,ID>> versionMap = new HashMap<>();
    private NavigableSet<Integer> versions = new TreeSet<Integer>();

    public void setPlugins(List<ApiVersionPlugin<C,P,ID>> apiVersionPlugins)
    {
        for (ApiVersionPlugin<C,P,ID> plugin : apiVersionPlugins)
        {
            if(versionMap.put(plugin.getVersion(), plugin) != null)
                throw new RuntimeException(String.format(
                        "Duplicate api plugin registered for version %d",plugin.getVersion()));
            versions.add(plugin.getVersion());
        }
    }

    public ApiVersionPlugin<C,P,ID> getPluginForVersion(Integer version)
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

    public boolean isValid(Integer version)
    {
        return versions.contains(version);
    }

    public List<Integer> getVersions()
    {
        return new ArrayList<>(versions);
    }

    public Integer getLatestVersion()
    {
        return versions.last();
    }
}
