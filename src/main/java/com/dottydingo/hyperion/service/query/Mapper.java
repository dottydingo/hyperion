package com.dottydingo.hyperion.service.query;

public interface Mapper
{

    /**
     * Translate given selector to the mapped property name or dot-separated
     * path of the property.
     *
     * @param selector    Selector that identifies some element of an entry's content.
     * @param entityClass entity class
     * @return Property name or dot-separated path of the property.
     */
    String translate(String selector, Class<?> entityClass);
}
