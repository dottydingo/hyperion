package com.dottydingo.hyperion.client;

import java.util.*;

/**
 */
public class MultiMap
{
    private Map<String,List<String>> map = new TreeMap<String,List<String>>(new CaseInsensitiveComparator());

    public List<String> get(String key)
    {
        if(key == null)
            throw new IllegalArgumentException("Null keys not allowed.");

        return map.get(key);
    }

    public String getFirst(String key)
    {
        if(key == null)
            throw new IllegalArgumentException("Null keys not allowed.");

        List<String> vals = map.get(key);
        if(vals == null || vals.size() == 0)
            return null;

        return vals.get(0);
    }

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

    public Set<String> getKeys()
    {
        return map.keySet();
    }

    public Set<Map.Entry<String,List<String>>> entries()
    {
        return map.entrySet();
    }

    public MultiMap merge(MultiMap multiMap)
    {
        MultiMap result = new MultiMap();
        result.map.putAll(this.map);
        result.map.putAll(multiMap.map);
        return result;
    }

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
