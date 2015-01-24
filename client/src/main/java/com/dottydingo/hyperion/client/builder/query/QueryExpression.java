package com.dottydingo.hyperion.client.builder.query;

/**
 */
public interface QueryExpression
{
    boolean isComplex();
    String build();
}
