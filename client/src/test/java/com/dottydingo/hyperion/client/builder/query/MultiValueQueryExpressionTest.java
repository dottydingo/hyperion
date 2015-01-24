package com.dottydingo.hyperion.client.builder.query;

import org.junit.Test;

import static org.junit.Assert.*;

public class MultiValueQueryExpressionTest
{

    @Test
    public void testBuild() throws Exception
    {
        assertEquals("foo=in=(bar,baz)",new MultiValueQueryExpression("foo",ComparisonOperator.IN,asArray("bar","baz")).build());
        assertEquals("foo=out=(bar,baz)",new MultiValueQueryExpression("foo",ComparisonOperator.NOT_IN,asArray("bar","baz")).build());
    }

    private <T> T[] asArray(T... items)
    {
        return items;
    }
}