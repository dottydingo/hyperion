package com.dottydingo.hyperion.service.pipeline;

import junit.framework.Assert;
import org.junit.Test;

/**
 */
public class UriParserTest
{
    private UriParser uriParser = new UriParser();

    @Test
    public void testParseRequestUri() throws Exception
    {
        assertResult("foo",false,null,uriParser.parseRequestUri("/foo"));
        assertResult("foo",false,null,uriParser.parseRequestUri("/foo/"));
        assertResult("foo",false,"123",uriParser.parseRequestUri("/foo/123"));
        assertResult("foo",false,"123",uriParser.parseRequestUri("/foo/123/"));
        assertResult("foo",false,"123,222",uriParser.parseRequestUri("/foo/123,222"));

        assertResult("foo",true,"123",uriParser.parseRequestUri("/foo/history/123"));
        assertResult("foo",true,"12345",uriParser.parseRequestUri("/foo/history/12345/"));

        Assert.assertNull(uriParser.parseRequestUri(""));
        Assert.assertNull(uriParser.parseRequestUri("/"));
        Assert.assertNull(uriParser.parseRequestUri("//"));
        Assert.assertNull(uriParser.parseRequestUri("//123"));
        Assert.assertNull(uriParser.parseRequestUri("/foo/history/"));
    }

    private void assertResult(String endpoint,boolean audit,String id,UriRequestResult result)
    {
        Assert.assertNotNull(result);
        Assert.assertEquals(endpoint,result.getEndpoint());
        Assert.assertEquals(audit,result.isHistory());
        Assert.assertEquals(id,result.getId());
    }
}
