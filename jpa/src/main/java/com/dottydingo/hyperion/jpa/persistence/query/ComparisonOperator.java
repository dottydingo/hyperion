package com.dottydingo.hyperion.jpa.persistence.query;

/**
 */
public enum ComparisonOperator
{
    NOT_EQUAL(false),
    GREATER_THAN(false),
    GREATER_EQUAL(false),
    LESS_THAN(false),
    LESS_EQUAL(false),
    EQUAL(false),
    IN(true),
    NOT_IN(true);

    private boolean supportsMultipleArguments;

    ComparisonOperator(boolean supportsMultipleArguments)
    {
        this.supportsMultipleArguments = supportsMultipleArguments;
    }

    public boolean supportsMultipleArguments()
    {
        return supportsMultipleArguments;
    }
}
