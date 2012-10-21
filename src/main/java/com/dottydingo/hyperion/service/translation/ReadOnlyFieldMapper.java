package com.dottydingo.hyperion.service.translation;

import com.dottydingo.hyperion.service.context.RequestContext;

/**
 * A field mapper that prevents writing to read only fields
 */
public class ReadOnlyFieldMapper<C,P> extends DefaultFieldMapper<C,P>
{
    public ReadOnlyFieldMapper(String name)
    {
        super(name);
    }

    public ReadOnlyFieldMapper(String clientFieldName, String persistentFieldName,ValueConverter valueConverter)
    {
        super(clientFieldName, persistentFieldName, valueConverter);
    }

    @Override
    public void convertToPersistent(ObjectWrapper<C> clientObjectWrapper, ObjectWrapper<P> persistentObjectWrapper, RequestContext context)
    {
        // no op
    }
}
