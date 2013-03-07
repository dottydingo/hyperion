package com.dottydingo.hyperion.service.key;

import com.dottydingo.hyperion.exception.BadRequestException;

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
