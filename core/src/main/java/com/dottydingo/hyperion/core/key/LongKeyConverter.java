package com.dottydingo.hyperion.core.key;

import com.dottydingo.hyperion.api.exception.BadRequestException;

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
            throw new BadRequestException(e.getMessage());
        }
    }
}
