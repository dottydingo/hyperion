package com.dottydingo.hyperion.client.builder.query;

import com.dottydingo.hyperion.client.exception.ClientException;

import java.util.regex.Pattern;

/**
 * Convenience class for creating query expressions
 */
public class QueryBuilder
{
    //'"' | "'" | "(" | ")" | ";" | "," | "=" | "!" | "~" | "<" | ">";
    private static final Pattern RESERVED = Pattern.compile("[\"'();,=!~<> ]");

    private static final String SINGLE_QUOTE = "'";
    private static final String DOUBLE_QUOTE = "\"";
    private static final String QUOTE_ERROR =
            String.format("Query value can not contain both %s and %s characters.", SINGLE_QUOTE, DOUBLE_QUOTE);

    /**
     * Create an equals expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression eq(String propertyName,String value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.EQUAL,wrap(value));
    }

    /**
     * Create an equals expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression eq(String propertyName,boolean value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.EQUAL, String.valueOf(value));
    }

    /**
     * Create an equals expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression eq(String propertyName,int value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.EQUAL, String.valueOf(value));
    }

    /**
     * Create an equals expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression eq(String propertyName,double value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.EQUAL, String.valueOf(value));
    }

    /**
     * Create an equals expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression eq(String propertyName,long value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.EQUAL, String.valueOf(value));
    }

    /**
     * Create an equals expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression eq(String propertyName,float value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.EQUAL, String.valueOf(value));
    }

    /**
     * Create a not equals expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression ne(String propertyName,String value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.NOT_EQUAL,wrap(value));
    }

    /**
     * Create a not equals expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression ne(String propertyName,boolean value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.NOT_EQUAL, String.valueOf(value));
    }

    /**
     * Create a not equals expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression ne(String propertyName,int value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.NOT_EQUAL, String.valueOf(value));
    }

    /**
     * Create a not equals expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression ne(String propertyName,double value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.NOT_EQUAL, String.valueOf(value));
    }

    /**
     * Create a not equals expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression ne(String propertyName,long value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.NOT_EQUAL, String.valueOf(value));
    }

    /**
     * Create a not equals expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression ne(String propertyName,float value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.NOT_EQUAL, String.valueOf(value));
    }

    /**
     * Create a greater than expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression gt(String propertyName,String value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.GREATER_THAN,wrap(value));
    }

    /**
     * Create a greater than expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression gt(String propertyName,int value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.GREATER_THAN, String.valueOf(value));
    }

    /**
     * Create a greater than expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression gt(String propertyName,double value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.GREATER_THAN, String.valueOf(value));
    }

    /**
     * Create a greater than expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression gt(String propertyName,long value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.GREATER_THAN, String.valueOf(value));
    }

    /**
     * Create a greater than expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression gt(String propertyName,float value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.GREATER_THAN, String.valueOf(value));
    }

    /**
     * Create a less than expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression lt(String propertyName,String value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.LESS_THAN,wrap(value));
    }

    /**
     * Create a less than expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression lt(String propertyName,int value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.LESS_THAN, String.valueOf(value));
    }

    /**
     * Create a less than expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression lt(String propertyName,double value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.LESS_THAN, String.valueOf(value));
    }

    /**
     * Create a less than expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression lt(String propertyName,long value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.LESS_THAN, String.valueOf(value));
    }

    /**
     * Create a less than expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression lt(String propertyName,float value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.LESS_THAN, String.valueOf(value));
    }

    /**
     * Create a greater than or equals expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression ge(String propertyName,String value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.GREATER_THAN_OR_EQUAL,wrap(value));
    }

    /**
     * Create a greater than or equals expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression ge(String propertyName,int value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.GREATER_THAN_OR_EQUAL, String.valueOf(value));
    }

    /**
     * Create a greater than or equals expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression ge(String propertyName,double value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.GREATER_THAN_OR_EQUAL, String.valueOf(value));
    }

    /**
     * Create a greater than or equals expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression ge(String propertyName,long value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.GREATER_THAN_OR_EQUAL, String.valueOf(value));
    }

    /**
     * Create a greater than or equals expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression ge(String propertyName,float value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.GREATER_THAN_OR_EQUAL, String.valueOf(value));
    }

    /**
     * Create a less than or equals expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression le(String propertyName,String value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.LESS_THAN_OR_EQUAL,wrap(value));
    }

    /**
     * Create a less than or equals expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression le(String propertyName,int value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.LESS_THAN_OR_EQUAL, String.valueOf(value));
    }

    /**
     * Create a less than or equals expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression le(String propertyName,double value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.LESS_THAN_OR_EQUAL, String.valueOf(value));
    }

    /**
     * Create a less than or equals expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression le(String propertyName,long value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.LESS_THAN_OR_EQUAL, String.valueOf(value));
    }

    /**
     * Create a less than or equals expression
     * @param propertyName The propery name
     * @param value The value
     * @return The query expression
     */
    public QueryExpression le(String propertyName,float value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.LESS_THAN_OR_EQUAL, String.valueOf(value));
    }

    /**
     * Create an in expression
     * @param propertyName The propery name
     * @param values The values
     * @return The query expression
     */
    public QueryExpression in(String propertyName,String... values)
    {
        return new MultiValueQueryExpression(propertyName, ComparisonOperator.IN,wrap(values));
    }


    /**
     * Create an in expression
     * @param propertyName The propery name
     * @param values The values
     * @return The query expression
     */
    public QueryExpression in(String propertyName,int... values)
    {
        return new MultiValueQueryExpression(propertyName, ComparisonOperator.IN, wrap(values));
    }

    /**
     * Create an in expression
     * @param propertyName The propery name
     * @param values The values
     * @return The query expression
     */
    public QueryExpression in(String propertyName,double... values)
    {
        return new MultiValueQueryExpression(propertyName, ComparisonOperator.IN, wrap(values));
    }

    /**
     * Create an in expression
     * @param propertyName The propery name
     * @param values The values
     * @return The query expression
     */
    public QueryExpression in(String propertyName,long... values)
    {
        return new MultiValueQueryExpression(propertyName, ComparisonOperator.IN, wrap(values));
    }

    /**
     * Create an in expression
     * @param propertyName The propery name
     * @param values The values
     * @return The query expression
     */
    public QueryExpression in(String propertyName,float... values)
    {
        return new MultiValueQueryExpression(propertyName, ComparisonOperator.IN, wrap(values));
    }

    /**
     * Create a not in expression
     * @param propertyName The propery name
     * @param values The values
     * @return The query expression
     */
    public QueryExpression notIn(String propertyName,String... values)
    {
        return new MultiValueQueryExpression(propertyName, ComparisonOperator.NOT_IN,wrap(values));
    }


    /**
     * Create a not in expression
     * @param propertyName The propery name
     * @param values The values
     * @return The query expression
     */
    public QueryExpression notIn(String propertyName,int... values)
    {
        return new MultiValueQueryExpression(propertyName, ComparisonOperator.NOT_IN, wrap(values));
    }

    /**
     * Create a not in expression
     * @param propertyName The propery name
     * @param values The values
     * @return The query expression
     */
    public QueryExpression notIn(String propertyName,double... values)
    {
        return new MultiValueQueryExpression(propertyName, ComparisonOperator.NOT_IN, wrap(values));
    }

    /**
     * Create a not in expression
     * @param propertyName The propery name
     * @param values The values
     * @return The query expression
     */
    public QueryExpression notIn(String propertyName,long... values)
    {
        return new MultiValueQueryExpression(propertyName, ComparisonOperator.NOT_IN, wrap(values));
    }

    /**
     * Create a not in expression
     * @param propertyName The propery name
     * @param values The values
     * @return The query expression
     */
    public QueryExpression notIn(String propertyName,float... values)
    {
        return new MultiValueQueryExpression(propertyName, ComparisonOperator.NOT_IN, wrap(values));
    }

    /**
     * Create an and expression
     * @return The query expressions
     */
    public QueryExpression and(QueryExpression... expressions)
    {
        return new LogicalQueryExpression(LogicalOperator.AND, expressions);
    }

    /**
     * Create an or expression
     * @return The query expressions
     */
    public QueryExpression or(QueryExpression... expressions)
    {
        return new LogicalQueryExpression(LogicalOperator.OR, expressions);
    }

    protected String wrap(String value)
    {
        if(value == null)
            throw new ClientException("Value can not be null.");

        if(!RESERVED.matcher(value).find())
            return value;


        if(value.contains(DOUBLE_QUOTE))
        {
            if(value.contains(SINGLE_QUOTE))
                throw new ClientException(QUOTE_ERROR);
            return SINGLE_QUOTE + value + SINGLE_QUOTE;
        }
        else if(value.contains(SINGLE_QUOTE))
        {
            if(value.contains(DOUBLE_QUOTE))
                throw new ClientException(QUOTE_ERROR);

            return DOUBLE_QUOTE + value + DOUBLE_QUOTE;
        }

        return SINGLE_QUOTE + value + SINGLE_QUOTE;
    }

    protected String[] wrap(String[] values)
    {
        String[] result = new String[values.length];
        for (int i = 0; i < values.length; i++)
        {
            result[i] = wrap(values[i]);
        }
        return result;
    }

    protected String[] wrap(int[] values)
    {
        String[] result = new String[values.length];
        for (int i = 0; i < values.length; i++)
        {
            result[i] = String.valueOf(values[i]);
        }
        return result;
    }

    protected String[] wrap(long[] values)
    {
        String[] result = new String[values.length];
        for (int i = 0; i < values.length; i++)
        {
            result[i] = String.valueOf(values[i]);
        }
        return result;
    }

    protected String[] wrap(double[] values)
    {
        String[] result = new String[values.length];
        for (int i = 0; i < values.length; i++)
        {
            result[i] = String.valueOf(values[i]);
        }
        return result;
    }

    protected String[] wrap(float[] values)
    {
        String[] result = new String[values.length];
        for (int i = 0; i < values.length; i++)
        {
            result[i] = String.valueOf(values[i]);
        }
        return result;
    }
}
