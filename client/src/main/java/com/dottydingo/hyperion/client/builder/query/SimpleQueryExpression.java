package com.dottydingo.hyperion.client.builder.query;

/**
 * A simple query expression
 */
public class SimpleQueryExpression implements QueryExpression
{
    private String propertyName;
    private ComparisonOperator comparisonOperator;
    private String value;

    /**
     * Create the query expression with the supplied values
     * @param propertyName The propery name
     * @param comparisonOperator The comparison operator
     * @param value THe value
     */
    public SimpleQueryExpression(String propertyName, ComparisonOperator comparisonOperator, String value)
    {
        this.propertyName = propertyName;
        this.comparisonOperator = comparisonOperator;
        this.value = value;
    }

    @Override
    public boolean isComplex()
    {
        return false;
    }

    @Override
    public String build()
    {
        return propertyName + comparisonOperator.getValue() + value;
    }
}
