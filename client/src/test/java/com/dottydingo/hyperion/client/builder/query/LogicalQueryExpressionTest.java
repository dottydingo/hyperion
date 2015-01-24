package com.dottydingo.hyperion.client.builder.query;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class LogicalQueryExpressionTest
{

    private static final SimpleQueryExpression one = new SimpleQueryExpression("foo", ComparisonOperator.EQUAL,"bar");
    private static final SimpleQueryExpression two = new SimpleQueryExpression("baz", ComparisonOperator.EQUAL,"buzz");

    @Test
    public void testBuildSimple() throws Exception
    {
        assertEquals("foo==bar;baz==buzz",new LogicalQueryExpression(LogicalOperator.AND,asArray(one,two)).build());
        assertEquals("foo==bar,baz==buzz",new LogicalQueryExpression(LogicalOperator.OR,asArray(one,two)).build());
    }

    @Test
    public void testBuildComplex() throws Exception
    {
        LogicalQueryExpression logicalQueryExpression = new LogicalQueryExpression(LogicalOperator.AND,asArray(two,one));
        assertEquals("(baz==buzz;foo==bar),baz==buzz",new LogicalQueryExpression(LogicalOperator.OR,asArray(logicalQueryExpression,two)).build());
    }

    private <T> T[] asArray(T... items)
    {
        return items;
    }
}