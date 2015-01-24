package com.dottydingo.hyperion.client.builder.query;

/**
 */
public enum LogicalOperator
{
    AND(";"),
    OR(",");


    public String getValue()
    {
        return value;
    }

    private String value;

    LogicalOperator(String value)
    {
        this.value = value;
    }
}
