package com.dottydingo.hyperion.service.key;

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
