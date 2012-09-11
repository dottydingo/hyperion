package com.dottydingo.hyperion.service.translation;

import net.sf.cglib.beans.BeanMap;

/**
 * A field mapper that prevents writing to read only fields
 */
public class ReadOnlyFieldMapper<C,P> extends DefaultFieldMapper<C,P>
{
    public ReadOnlyFieldMapper(String name, BeanMap clientBeanMap, BeanMap persistentBeanMap)
    {
        super(name, clientBeanMap, persistentBeanMap);
    }

    public ReadOnlyFieldMapper(String clientFieldName, String persistentFieldName, BeanMap clientBeanMap,
                               BeanMap persistentBeanMap, ValueConverter valueConverter)
    {
        super(clientFieldName, persistentFieldName, clientBeanMap, persistentBeanMap, valueConverter);
    }

    @Override
    public void convertToPersistent(C clientObject, P persistentObject, TranslationContext context)
    {
        // no-op
    }
}
