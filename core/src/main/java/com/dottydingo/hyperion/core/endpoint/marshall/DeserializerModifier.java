package com.dottydingo.hyperion.core.endpoint.marshall;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;

import java.util.Iterator;

/**
 */
public class DeserializerModifier extends BeanDeserializerModifier
{
    @Override
    public BeanDeserializerBuilder updateBuilder(DeserializationConfig config, BeanDescription beanDesc, BeanDeserializerBuilder builder)
    {
        Iterator<SettableBeanProperty> propertyIterator = builder.getProperties();
        while (propertyIterator.hasNext())
        {
            SettableBeanProperty next = propertyIterator.next();
            builder.addOrReplaceProperty(new TrackingSettableBeanProperty(next),true);
        }

        return builder;
    }
}
