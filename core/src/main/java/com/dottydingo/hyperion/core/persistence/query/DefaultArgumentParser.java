package com.dottydingo.hyperion.core.persistence.query;

import com.dottydingo.hyperion.api.exception.BadParameterException;
import com.dottydingo.hyperion.api.exception.HyperionException;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 */
public class DefaultArgumentParser  implements ArgumentParser
{
    public static final String PARAMETER_CONVERSION_ERROR = "ERROR_PARAMETER_CONVERSION_ERROR";
    private static DefaultArgumentParser INSTANCE = new DefaultArgumentParser();

    public static ArgumentParser getInstance() {return INSTANCE;}

    private static final Logger logger = LoggerFactory.getLogger(DefaultArgumentParser.class);

    private DateTimeFormatter dateParser = ISODateTimeFormat.dateOptionalTimeParser();

    @Override
    public <T> List<T> parse(List<String> argument, Class<T> type, PersistenceContext context) throws HyperionException
    {
        List<T> results = new ArrayList<>();
        for (String s : argument)
        {
            results.add(parse(s,type, context));
        }
        return results;
    }

    @Override
    public <T> T parse(String argument, Class<T> type, PersistenceContext context) throws HyperionException
    {

        logger.debug("Parsing argument '{}' as type {}", argument, type.getSimpleName());

        // common types
        try
        {
            if (type.equals(String.class))
            {
                return (T) argument;
            }
            if (type.equals(Integer.class) || type.equals(int.class))
            {
                return (T) Integer.valueOf(argument);
            }
            if (type.equals(Boolean.class) || type.equals(boolean.class))
            {
                return (T) Boolean.valueOf(argument);
            }
            if (type.equals(Float.class) || type.equals(float.class))
            {
                return (T) Float.valueOf(argument);
            }
            if (type.equals(Double.class) || type.equals(double.class))
            {
                return (T) Double.valueOf(argument);
            }
            if (type.equals(Long.class) || type.equals(long.class))
            {
                return (T) Long.valueOf(argument);
            }
        }
        catch (IllegalArgumentException ex)
        {
            throw new BadParameterException(createErrorMessage(argument, type, context));
        }

        // enums
        if (type.isEnum())
        {
            // first try the simple case
            try
            {
                return (T) Enum.valueOf((Class<Enum>) type, argument);
            }
            catch (Exception ignore){}

            // try all upper
            try
            {
                return (T) Enum.valueOf((Class<Enum>) type, argument.toUpperCase());
            }
            catch (Exception ignore){}

            // try all lower
            try
            {
                return (T) Enum.valueOf((Class<Enum>) type, argument.toLowerCase());
            }
            catch (Exception ignore){}

            throw new BadParameterException(createErrorMessage(argument, type, context));
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
                throw new BadParameterException(createErrorMessage(argument, type, context));
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
            throw new BadParameterException(createErrorMessage(argument, type, context),ex);
        }

        throw new BadParameterException(createErrorMessage(argument, type, context));
    }

    protected <T> String createErrorMessage(String argument, Class<T> type, PersistenceContext context)
    {
        return context.getMessageSource().getErrorMessage(PARAMETER_CONVERSION_ERROR,context.getLocale(),argument,type);
    }


}
