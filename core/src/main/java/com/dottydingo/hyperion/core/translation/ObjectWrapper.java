package com.dottydingo.hyperion.core.translation;

/**
 */
public class ObjectWrapper<T>
{
    private T wrappedObject;
    private TypeMapper typeMapper;

    public ObjectWrapper(T wrappedObject, TypeMapper typeMapper)
    {
        this.wrappedObject = wrappedObject;
        this.typeMapper = typeMapper;
    }

    public void setValue(String property,Object value)
    {
        typeMapper.setValue(wrappedObject, property, value);
    }

    public Object getValue(String property)
    {
        return typeMapper.getValue(wrappedObject,property);
    }

    public T getWrappedObject()
    {
        return wrappedObject;
    }

    public TypeMapper getTypeMapper()
    {
        return typeMapper;
    }
}
