package com.dottydingo.hyperion.api;

import java.util.List;

/**
 * A list of entities that can be passed to or from the service
 */
public class EntityList<T extends ApiObject>
{
    private List<T> entries;

    /**
     * Return the entities
     * @return the entities
     */
    public List<T> getEntries()
    {
        return entries;
    }

    /**
     * Set the entities
     * @param entries The entities
     */
    public void setEntries(List<T> entries)
    {
        this.entries = entries;
    }
}
