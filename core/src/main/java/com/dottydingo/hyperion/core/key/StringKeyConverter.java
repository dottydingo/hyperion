package com.dottydingo.hyperion.core.key;

/**
 */
public class StringKeyConverter extends AbstractKeyConverter<String>
{
    @Override
    protected String convertValue(String value)
    {
        return value;
    }
}
