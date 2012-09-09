package com.dottydingo.hyperion.service.key;

/**
 */
public class IntegerKeyConverter extends AbstractKeyConverter<Integer>
{
    @Override
    protected Integer convertValue(String value)
    {
        return Integer.parseInt(value);
    }
}
