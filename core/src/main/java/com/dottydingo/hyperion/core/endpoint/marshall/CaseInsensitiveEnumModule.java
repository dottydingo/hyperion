package com.dottydingo.hyperion.core.endpoint.marshall;

import com.dottydingo.hyperion.api.CaseInsensitiveEnum;
import com.dottydingo.hyperion.api.exception.InternalException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class CaseInsensitiveEnumModule extends SimpleModule
{
    @Override
    public void setupModule(SetupContext context)
    {
        super.setupModule(context);
        context.addBeanDeserializerModifier(new BeanDeserializerModifier()
        {
            @Override
            public JsonDeserializer<Enum> modifyEnumDeserializer(DeserializationConfig config,
                                                                 final JavaType type,
                                                                 BeanDescription beanDesc,
                                                                 final JsonDeserializer<?> deserializer)
            {
                if (beanDesc.getClassAnnotations().get(CaseInsensitiveEnum.class) == null)
                {
                    return (JsonDeserializer<Enum>) deserializer;
                }


                return new CaseInsensitiveDeserializer(type);

            }


        });
    }

    private class CaseInsensitiveDeserializer extends JsonDeserializer<Enum>
    {
        private final JavaType javaType;
        private final Map<String,Enum> map = new HashMap<>();

        public CaseInsensitiveDeserializer(JavaType javaType)
        {
            this.javaType = javaType;

            Enum[] values = (Enum[]) javaType.getRawClass().getEnumConstants();
            for (Enum value : values)
            {
                if(map.put(value.name().toUpperCase(),value) != null)
                    throw new InternalException(
                            String.format("Enum %s is marked as case insensitive but contains duplicate values.",
                                    javaType.getRawClass().getName()));

            }


        }

        @Override
        public Enum deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException
        {
            String stringValue = jp.getValueAsString().toUpperCase();

            Enum value = map.get(stringValue);

            if(value == null)
            {
                String name = jp.getText();
                throw ctxt.weirdStringException(name, javaType.getRawClass(),
                        "value not one of declared Enum instance names: " + map.values());
            }

            return value;
        }
    }
}
