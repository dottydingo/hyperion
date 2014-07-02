package com.dottydingo.hyperion.core.key;

import com.dottydingo.hyperion.api.exception.BadRequestException;

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
            throw new BadRequestException(e.getMessage());
        }
    }
}
