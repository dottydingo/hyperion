package com.dottydingo.hyperion.client.builder.query;

/**
 */
public class LogicalQueryExpression implements QueryExpression
{
    private QueryExpression[] expressions;
    private LogicalOperator operator;

    public LogicalQueryExpression(LogicalOperator operator, QueryExpression[] expressions)
    {
        this.expressions = expressions;
        this.operator = operator;
    }

    @Override
    public boolean isComplex()
    {
        return true;
    }

    @Override
    public String build()
    {
        StringBuilder sb = new StringBuilder(512);
        for (int i = 0; i < expressions.length; i++)
        {
            QueryExpression expression = expressions[i];

            if(i>0)
                sb.append(operator.getValue());

            if(expression.isComplex())
                sb.append("(");
            sb.append(expression.build());
            if(expression.isComplex())
                sb.append(")");
        }
        return sb.toString();
    }
}
