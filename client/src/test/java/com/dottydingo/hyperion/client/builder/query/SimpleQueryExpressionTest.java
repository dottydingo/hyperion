package com.dottydingo.hyperion.client.builder.query;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class SimpleQueryExpressionTest
{

    @Test
    public void testBuild() throws Exception
    {
        assertEquals("foo==bar",new SimpleQueryExpression("foo", ComparisonOperator.EQUAL,"bar").build());
        assertEquals("foo!=bar",new SimpleQueryExpression("foo", ComparisonOperator.NOT_EQUAL,"bar").build());
        assertEquals("foo=gt=bar",new SimpleQueryExpression("foo", ComparisonOperator.GREATER_THAN,"bar").build());
        assertEquals("foo=ge=bar",new SimpleQueryExpression("foo", ComparisonOperator.GREATER_THAN_OR_EQUAL,"bar").build());
        assertEquals("foo=lt=bar",new SimpleQueryExpression("foo", ComparisonOperator.LESS_THAN,"bar").build());
        assertEquals("foo=le=bar",new SimpleQueryExpression("foo", ComparisonOperator.LESS_THAN_OR_EQUAL,"bar").build());
    }
}