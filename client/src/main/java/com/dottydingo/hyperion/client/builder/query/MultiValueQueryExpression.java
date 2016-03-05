package com.dottydingo.hyperion.client.builder.query;

/**
 * A query expression that supports multiple values
 */
public class MultiValueQueryExpression implements QueryExpression
{
    private String propertyName;
    private ComparisonOperator comparisonOperator;
    private String[] values;

    /**
     * Create the query expression with the supplied values
     * @param propertyName The propery name
     * @param comparisonOperator The comparison operator
     * @param values The values
     */
    public MultiValueQueryExpression(String propertyName, ComparisonOperator comparisonOperator, String[] values)
    {
        this.propertyName = propertyName;
        this.comparisonOperator = comparisonOperator;
        this.values = values;
    }

    @Override
    public boolean isComplex()
    {
        return false;
    }

    @Override
    public String build()
    {
        StringBuilder sb = new StringBuilder(512);

        sb.append(propertyName).append(comparisonOperator.getValue()).append("(");
        for (int i = 0; i < values.length; i++)
        {
            String value = values[i];
            if(i > 0)
                sb.append(",");
            sb.append(value);
        }
        sb.append(")");

        return sb.toString();
    }
}
