package com.dottydingo.hyperion.service.translation;

import net.sf.cglib.beans.BeanMap;

/**
 */
public class ObjectWrapper<T>
{
    private T wrappedObject;
    private BeanMap beanMap;

    public ObjectWrapper(T wrappedObject, BeanMap beanMap)
    {
        this.wrappedObject = wrappedObject;
        this.beanMap = beanMap;
    }

    public void setValue(String property,Object value)
    {
        beanMap.put(wrappedObject, property, value);
    }

    public Object getValue(String property)
    {
        return beanMap.get(wrappedObject,property);
    }

    public T getWrappedObject()
    {
        return wrappedObject;
    }

    public BeanMap getBeanMap()
    {
        return beanMap;
    }
}
