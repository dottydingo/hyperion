package com.dottydingo.hyperion.core.key;


/**
 */
public class IntegerKeyConverter extends AbstractKeyConverter<Integer>
{
    @Override
    protected Integer convertValue(String value)
    {
        try
        {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException e)
        {
            throw new KeyConverterException(value);
        }
    }
}
