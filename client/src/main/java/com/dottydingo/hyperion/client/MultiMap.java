package com.dottydingo.hyperion.client;

import java.util.*;

/**
 * A map that can hold multiple values for the same key.
 */
public class MultiMap
{
    private Map<String,List<String>> map = new TreeMap<String,List<String>>(new CaseInsensitiveComparator());

    /**
     * Return the values stored for the supplied key
     * @param key The key
     * @return The values stored for this key, null if there are no values stored
     */
    public List<String> get(String key)
    {
        if(key == null)
            throw new IllegalArgumentException("Null keys not allowed.");

        return map.get(key);
    }

    /**
     * Return the first value stored for the supplied key
     * @param key The key
     * @return the first value stored for the key, null if there are no values stored
     */
    public String getFirst(String key)
    {
        if(key == null)
            throw new IllegalArgumentException("Null keys not allowed.");

        List<String> vals = map.get(key);
        if(vals == null || vals.size() == 0)
            return null;

        return vals.get(0);
    }

    /**
     * Set a value for the supplied key. This will overwrite any values that are currently stored for this key.
     * @param key The key
     * @param value The value to store
     */
    public void set(String key, String value)
    {
        if(key == null)
            throw new IllegalArgumentException("Null keys not allowed.");

        if(value == null)
            throw new IllegalArgumentException("Null values not allowed.");

        ArrayList<String> vals = new ArrayList<String>();
        vals.add(value);
        map.put(key,vals);

    }

    /**
     * Add a value for the supplied key. This will add the value to any existing values stored for this key.
     * @param key the key
     * @param value The value
     */
    public void add(String key, String value)
    {
        if(key == null)
            throw new IllegalArgumentException("Null keys not allowed.");

        if(value == null)
            throw new IllegalArgumentException("Null values not allowed.");

        List<String> vals = map.get(key);
        if(vals == null)
        {
            vals = new ArrayList<String>();
            map.put(key,vals);
        }

        vals.add(value);
    }

    /**
     * Return the keys stored in this map
     * @return The keys
     */
    public Set<String> getKeys()
    {
        return map.keySet();
    }

    /**
     * Return the entry set for this map
     * @return The entry set
     */
    public Set<Map.Entry<String,List<String>>> entries()
    {
        return map.entrySet();
    }

    /**
     * Merge the values in the specified map with the values in this map and return the results in a new map.
     * @param multiMap The map to merge
     * @return A new map containing the merged values
     */
    public MultiMap merge(MultiMap multiMap)
    {
        MultiMap result = new MultiMap();
        result.map.putAll(this.map);
        result.map.putAll(multiMap.map);
        return result;
    }

    /**
     * Return a flag indicating if the map is empty
     * @return True if empty, false otherwise
     */
    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    private class CaseInsensitiveComparator implements Comparator<String>
    {
        @Override
        public int compare(String o1, String o2)
        {
            return o1.toLowerCase().compareTo(o2.toLowerCase());
        }
    }
}
