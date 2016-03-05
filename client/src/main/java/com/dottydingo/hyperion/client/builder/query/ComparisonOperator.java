package com.dottydingo.hyperion.client.builder.query;

/**
 * Comparison operators
 */
public enum ComparisonOperator
{
    EQUAL("=="),
    NOT_EQUAL("!="),
    GREATER_THAN("=gt="),
    GREATER_THAN_OR_EQUAL("=ge="),
    LESS_THAN("=lt="),
    LESS_THAN_OR_EQUAL("=le="),
    IN("=in="),
    NOT_IN("=out=");

    private String value;

    ComparisonOperator(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}
