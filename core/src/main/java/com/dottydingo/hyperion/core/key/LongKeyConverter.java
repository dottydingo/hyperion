package com.dottydingo.hyperion.core.key;

/**
 */
public class LongKeyConverter extends AbstractKeyConverter<Long>
{
    @Override
    protected Long convertValue(String value)
    {
        try
        {
            return Long.parseLong(value);
        }
        catch (NumberFormatException e)
        {
            throw new KeyConverterException(value);
        }
    }
}
