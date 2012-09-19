package com.dottydingo.hyperion.service.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * User: mark
 * Date: 9/16/12
 * Time: 4:24 PM
 */
public class SimpleMapper implements Mapper
{
    private static final Logger logger = LoggerFactory.getLogger(SimpleMapper.class);

    private Map<Class<?>, Map<String, String>> mapping;


    /**
     * Construct new <tt>SimpleMapper</tt>
     */
    public SimpleMapper()
    {
        mapping = new HashMap<Class<?>, Map<String, String>>();
    }


    @Override
    public String translate(String selector, Class<?> entityClass)
    {
        if (mapping.isEmpty())
        {
            return selector;
        }

        Map<String, String> map = mapping.get(entityClass);
        String property = (map != null) ? map.get(selector) : null;

        if (property != null)
        {
            logger.debug("Found mapping {} -> {}", selector, property);
            return property;
        }

        return selector;
    }


    /**
     * Add selectors -> property names mapping for given entity class.
     *
     * @param entityClass entity class
     * @param mapping     mapping of selectors to property names
     */
    public void addMapping(Class<?> entityClass, Map<String, String> mapping)
    {
        this.mapping.put(entityClass, mapping);
    }

    /**
     * Add one selector -> property name mapping for given entity class.
     *
     * @param entityClass entity class
     * @param selector    Selector that identifies some element of an entry's content.
     * @param property    Name of corresponding entity's property.
     */
    public void addMapping(Class<?> entityClass, String selector, String property)
    {
        mapping.get(entityClass).put(selector, property);
    }

    /**
     * @return The current mapping of all entities.
     * @see SimpleMapper#setMapping(java.util.Map)
     */
    public Map<Class<?>, Map<String, String>> getMapping()
    {
        return mapping;
    }

    /**
     * Set the mapping of selectors to property names per entity class.
     *
     * @param mapping {entity class -> {selector -> property}}
     */
    public void setMapping(Map<Class<?>, Map<String, String>> mapping)
    {
        this.mapping = mapping;
    }
}
