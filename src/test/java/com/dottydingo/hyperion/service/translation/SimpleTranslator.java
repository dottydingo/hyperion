package com.dottydingo.hyperion.service.translation;

/**
 */
public class SimpleTranslator extends BaseTranslator<SimpleClientObject,SimplePersistentObject>
{
    @Override
    protected SimpleClientObject createClientInstance()
    {
        return new SimpleClientObject();
    }

    @Override
    protected SimplePersistentObject createPersistentInstance()
    {
        return new SimplePersistentObject();
    }

}
