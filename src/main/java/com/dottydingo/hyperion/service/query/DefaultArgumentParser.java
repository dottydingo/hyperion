package com.dottydingo.hyperion.service.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 */
public class DefaultArgumentParser implements ArgumentParser
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultArgumentParser.class);

    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd"); //ISO 8601
    private static final DateFormat DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); //ISO 8601

    @Override
    public <T> T parse(String argument, Class<T> type)
            throws IllegalArgumentException, ArgumentFormatException
    {

        LOG.trace("Parsing argument '{}' as type {}", argument, type.getSimpleName());

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
            throw new ArgumentFormatException(argument, type);
        }

        // date
        if (type.equals(Date.class))
        {
            try
            {
                return (T) DATE_TIME_FORMATTER.parse(argument);
            }
            catch (ParseException ex)
            {
            }
            try
            {
                return (T) DATE_FORMATTER.parse(argument);
            }
            catch (ParseException ex1)
            {
                throw new ArgumentFormatException(argument, type);
            }
        }

        // try to parse via valueOf(String s) method
        try
        {
            LOG.trace("Trying to get and invoke valueOf(String s) method on {}", type);
            Method method = type.getMethod("valueOf", String.class);
            return (T) method.invoke(type, argument);

        }
        catch (NoSuchMethodException ex)
        {
            LOG.warn("{} does not have method valueOf(String s)", type);
        }

        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }

        throw new InternalError("Cannot parse argument type " + type);
    }
}
