package com.dottydingo.hyperion.service.persistence.query;

import com.dottydingo.hyperion.exception.BadParameterException;
import com.dottydingo.hyperion.exception.HyperionException;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Date;

/**
 */
public class DefaultArgumentParser  implements ArgumentParser
{
    private static DefaultArgumentParser INSTANCE = new DefaultArgumentParser();

    public static ArgumentParser getInstance() {return INSTANCE;}

    private static final Logger logger = LoggerFactory.getLogger(DefaultArgumentParser.class);

    private DateTimeFormatter dateParser = ISODateTimeFormat.dateOptionalTimeParser();

    @Override
    public <T> T parse(String argument, Class<T> type) throws HyperionException
    {

        logger.debug("Parsing argument '{}' as type {}", argument, type.getSimpleName());

        // common types
        try
        {
            if (type.equals(String.class))
            {
                return (T) argument;
            }
            if (type.equals(Integer.class))
            {
                return (T) Integer.valueOf(argument);
            }
            if (type.equals(Boolean.class))
            {
                return (T) Boolean.valueOf(argument);
            }
            if (type.isEnum())
            {
                return (T) Enum.valueOf((Class<Enum>) type, argument);
            }
            if (type.equals(Float.class))
            {
                return (T) Float.valueOf(argument);
            }
            if (type.equals(Double.class))
            {
                return (T) Double.valueOf(argument);
            }
            if (type.equals(Long.class))
            {
                return (T) Long.valueOf(argument);
            }
        }
        catch (IllegalArgumentException ex)
        {
            throw new BadParameterException(String.format("Could not convert \"%s\" to a %s",argument,type.getSimpleName()));
        }

        // date
        if (type.equals(Date.class))
        {
            try
            {
                return (T) dateParser.parseDateTime(argument).toDate();
            }
            catch (IllegalArgumentException ex1)
            {
                throw new BadParameterException(String.format("Could not convert \"%s\" to a %s",argument,type.getSimpleName()));
            }
        }

        // try to parse via valueOf(String s) method
        try
        {
            logger.debug("Trying to get and invoke valueOf(String s) method on {}", type);
            Method method = type.getMethod("valueOf", String.class);
            return (T) method.invoke(type, argument);

        }
        catch (NoSuchMethodException ex)
        {
            logger.warn("{} does not have method valueOf(String s)", type);
        }
        catch (Exception ex)
        {
            throw new BadParameterException(String.format("Could not convert \"%s\" to a %s",argument,type.getSimpleName()),ex);
        }

        throw new BadParameterException(String.format("Could not convert \"%s\" to a %s",argument,type.getSimpleName()));
    }
}
