package com.dottydingo.hyperion.client.builder.query;

/**
 */
public class QueryBuilder
{
    public QueryExpression eq(String propertyName,String value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.EQUAL,wrap(value));
    }

    public QueryExpression eq(String propertyName,boolean value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.EQUAL, String.valueOf(value));
    }

    public QueryExpression eq(String propertyName,int value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.EQUAL, String.valueOf(value));
    }

    public QueryExpression eq(String propertyName,double value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.EQUAL, String.valueOf(value));
    }

    public QueryExpression eq(String propertyName,long value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.EQUAL, String.valueOf(value));
    }

    public QueryExpression eq(String propertyName,float value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.EQUAL, String.valueOf(value));
    }

    public QueryExpression ne(String propertyName,String value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.NOT_EQUAL,wrap(value));
    }

    public QueryExpression ne(String propertyName,boolean value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.NOT_EQUAL, String.valueOf(value));
    }

    public QueryExpression ne(String propertyName,int value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.NOT_EQUAL, String.valueOf(value));
    }

    public QueryExpression ne(String propertyName,double value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.NOT_EQUAL, String.valueOf(value));
    }

    public QueryExpression ne(String propertyName,long value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.NOT_EQUAL, String.valueOf(value));
    }

    public QueryExpression ne(String propertyName,float value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.NOT_EQUAL, String.valueOf(value));
    }

    public QueryExpression gt(String propertyName,String value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.GREATER_THAN,wrap(value));
    }

    public QueryExpression gt(String propertyName,int value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.GREATER_THAN, String.valueOf(value));
    }

    public QueryExpression gt(String propertyName,double value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.GREATER_THAN, String.valueOf(value));
    }

    public QueryExpression gt(String propertyName,long value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.GREATER_THAN, String.valueOf(value));
    }

    public QueryExpression gt(String propertyName,float value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.GREATER_THAN, String.valueOf(value));
    }

    public QueryExpression lt(String propertyName,String value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.LESS_THAN,wrap(value));
    }

    public QueryExpression lt(String propertyName,int value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.LESS_THAN, String.valueOf(value));
    }

    public QueryExpression lt(String propertyName,double value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.LESS_THAN, String.valueOf(value));
    }

    public QueryExpression lt(String propertyName,long value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.LESS_THAN, String.valueOf(value));
    }

    public QueryExpression lt(String propertyName,float value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.LESS_THAN, String.valueOf(value));
    }

    public QueryExpression ge(String propertyName,String value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.GREATER_THAN_OR_EQUAL,wrap(value));
    }

    public QueryExpression ge(String propertyName,int value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.GREATER_THAN_OR_EQUAL, String.valueOf(value));
    }

    public QueryExpression ge(String propertyName,double value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.GREATER_THAN_OR_EQUAL, String.valueOf(value));
    }

    public QueryExpression ge(String propertyName,long value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.GREATER_THAN_OR_EQUAL, String.valueOf(value));
    }

    public QueryExpression ge(String propertyName,float value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.GREATER_THAN_OR_EQUAL, String.valueOf(value));
    }

    public QueryExpression le(String propertyName,String value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.LESS_THAN_OR_EQUAL,wrap(value));
    }

    public QueryExpression le(String propertyName,int value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.LESS_THAN_OR_EQUAL, String.valueOf(value));
    }

    public QueryExpression le(String propertyName,double value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.LESS_THAN_OR_EQUAL, String.valueOf(value));
    }

    public QueryExpression le(String propertyName,long value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.LESS_THAN_OR_EQUAL, String.valueOf(value));
    }

    public QueryExpression le(String propertyName,float value)
    {
        return new SimpleQueryExpression(propertyName, ComparisonOperator.LESS_THAN_OR_EQUAL, String.valueOf(value));
    }

    public QueryExpression in(String propertyName,String... values)
    {
        return new MultiValueQueryExpression(propertyName, ComparisonOperator.IN,wrap(values));
    }


    public QueryExpression in(String propertyName,int... values)
    {
        return new MultiValueQueryExpression(propertyName, ComparisonOperator.IN, wrap(values));
    }

    public QueryExpression in(String propertyName,double... values)
    {
        return new MultiValueQueryExpression(propertyName, ComparisonOperator.IN, wrap(values));
    }

    public QueryExpression in(String propertyName,long... values)
    {
        return new MultiValueQueryExpression(propertyName, ComparisonOperator.IN, wrap(values));
    }

    public QueryExpression in(String propertyName,float... values)
    {
        return new MultiValueQueryExpression(propertyName, ComparisonOperator.IN, wrap(values));
    }

    public QueryExpression notIn(String propertyName,String... values)
    {
        return new MultiValueQueryExpression(propertyName, ComparisonOperator.NOT_IN,wrap(values));
    }


    public QueryExpression notIn(String propertyName,int... values)
    {
        return new MultiValueQueryExpression(propertyName, ComparisonOperator.NOT_IN, wrap(values));
    }

    public QueryExpression notIn(String propertyName,double... values)
    {
        return new MultiValueQueryExpression(propertyName, ComparisonOperator.NOT_IN, wrap(values));
    }

    public QueryExpression notIn(String propertyName,long... values)
    {
        return new MultiValueQueryExpression(propertyName, ComparisonOperator.NOT_IN, wrap(values));
    }

    public QueryExpression notIn(String propertyName,float... values)
    {
        return new MultiValueQueryExpression(propertyName, ComparisonOperator.NOT_IN, wrap(values));
    }

    public QueryExpression and(QueryExpression... expressions)
    {
        return new LogicalQueryExpression(LogicalOperator.AND, expressions);
    }

    public QueryExpression or(QueryExpression... expressions)
    {
        return new LogicalQueryExpression(LogicalOperator.OR, expressions);
    }

    protected String wrap(String value)
    {
        return value.contains(" ") ? "'" + value + "'" : value;
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
