package com.dottydingo.hyperion.jpa.persistence;


import org.junit.Assert;
import org.junit.Test;

/**
 */
public class PathIteratorTest
{
    @Test
    public void testPathIterator()
    {
        PathIterator path = PathIterator.getPath("foo");
        Assert.assertTrue(path.hasNext());
        Assert.assertEquals("foo",path.next());
        Assert.assertFalse(path.hasNext());

        path = PathIterator.getPath("foo.bar.baz");
        Assert.assertTrue(path.hasNext());
        Assert.assertEquals("foo",path.next());
        Assert.assertTrue(path.hasNext());
        Assert.assertEquals("bar",path.next());
        Assert.assertTrue(path.hasNext());
        Assert.assertEquals("baz",path.next());
        Assert.assertFalse(path.hasNext());
    }

    @Test
    public void testEmpty()
    {
        PathIterator path = PathIterator.getPath(null);
        Assert.assertFalse(path.hasNext());

        path = PathIterator.getPath("");
        Assert.assertFalse(path.hasNext());
    }
}
