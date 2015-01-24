package com.dottydingo.hyperion.client.builder.query;

/**
 */
public class SimpleQueryExpression implements QueryExpression
{
    private String propertyName;
    private ComparisonOperator comparisonOperator;
    private String value;

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
