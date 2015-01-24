package com.dottydingo.hyperion.client.builder.query;

/**
 */
public class MultiValueQueryExpression implements QueryExpression
{
    private String propertyName;
    private ComparisonOperator comparisonOperator;
    private String[] values;

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
