package com.dottydingo.hyperion.service.key;

/**
 */
public class LongKeyConverter extends AbstractKeyConverter<Long>
{
    @Override
    protected Long convertValue(String value)
    {
        return Long.parseLong(value);
    }
}
