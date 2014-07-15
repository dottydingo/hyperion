package com.dottydingo.hyperion.core.endpoint.marshall;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 */
public class TrackingSettableBeanProperty extends SettableBeanProperty
{
    public static final String PROVIDED_FIELDS_MAP = "SET_FIELDS";

    private SettableBeanProperty wrapped;

    public TrackingSettableBeanProperty(SettableBeanProperty wrapped)
    {
        super(wrapped);
        this.wrapped = wrapped;
    }

    @Override
    public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser)
    {
        return new TrackingSettableBeanProperty(wrapped.withValueDeserializer(deser));
    }

    @Override
    public SettableBeanProperty withName(PropertyName newName)
    {
        return wrapped.withName(newName);
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> acls)
    {
        return wrapped.getAnnotation(acls);
    }

    @Override
    public AnnotatedMember getMember()
    {
        return wrapped.getMember();
    }

    @Override
    public void deserializeAndSet(JsonParser jp, DeserializationContext ctxt, Object instance)
            throws IOException, JsonProcessingException
    {

        wrapped.deserializeAndSet(jp, ctxt, instance);
        trackField(ctxt, instance);
    }

    private void trackField(DeserializationContext ctxt, Object instance)
    {
        String name = getName();
        Map<Object,Set<String>> map = (Map<Object, Set<String>>) ctxt.getAttribute(PROVIDED_FIELDS_MAP);
        Set<String> set = map.get(instance);
        if(set == null)
        {
            set = new HashSet<>();
            map.put(instance,set);
        }
        set.add(name);
    }

    @Override
    public Object deserializeSetAndReturn(JsonParser jp, DeserializationContext ctxt, Object instance)
            throws IOException, JsonProcessingException
    {
        trackField(ctxt, instance);

        return wrapped.deserializeSetAndReturn(jp, ctxt, instance);
    }

    @Override
    public void set(Object instance, Object value) throws IOException
    {
        throw new RuntimeException("Not supported");
    }

    @Override
    public Object setAndReturn(Object instance, Object value) throws IOException
    {
        throw new RuntimeException("Not supported");
    }
}
