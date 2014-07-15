package com.dottydingo.hyperion.core.endpoint.marshall;

import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 */
public class CollectorModule extends SimpleModule
{
    @Override
    public void setupModule(SetupContext context)
    {
        super.setupModule(context);
        context.addBeanDeserializerModifier(new DeserializerModifier());
    }
}
