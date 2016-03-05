package com.dottydingo.hyperion.client.builder.query;

/**
 * A query expression
 */
public interface QueryExpression
{
    /**
     * A flag indicating if the query expression is complex
     * @return True if the expression is complex, false otherwise
     */
    boolean isComplex();

    /**
     * Return the RSQL representation of the query expression
     * @return The RSQL
     */
    String build();
}
