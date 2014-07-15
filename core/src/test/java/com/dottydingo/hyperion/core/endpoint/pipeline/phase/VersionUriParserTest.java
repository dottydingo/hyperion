package com.dottydingo.hyperion.core.endpoint.pipeline.phase;

import junit.framework.Assert;
import org.junit.Test;

/**
 */
public class VersionUriParserTest
{
    private VersionUriParser uriParser = new VersionUriParser();

    @Test
    public void testParseRequestUri() throws Exception
    {
        assertResult("foo",false,null, "1", uriParser.parseRequestUri("/foo/v1"));
        assertResult("foo",false,null, "1", uriParser.parseRequestUri("/foo/v1/"));
        assertResult("foo",false,"123", "1", uriParser.parseRequestUri("/foo/v1/123"));
        assertResult("foo",false,"123", "1", uriParser.parseRequestUri("/foo/v1/123/"));
        assertResult("foo",false,"123,222", "1", uriParser.parseRequestUri("/foo/v1/123,222"));

        assertResult("foo",true,"123", "1", uriParser.parseRequestUri("/foo/v1/history/123"));
        assertResult("foo",true,"12345", "1", uriParser.parseRequestUri("/foo/v1/history/12345/"));

        Assert.assertNull(uriParser.parseRequestUri(""));
        Assert.assertNull(uriParser.parseRequestUri("/"));
        Assert.assertNull(uriParser.parseRequestUri("///"));
        Assert.assertNull(uriParser.parseRequestUri("///123"));
        Assert.assertNull(uriParser.parseRequestUri("/foo/v1/history/"));
        Assert.assertNull(uriParser.parseRequestUri("/foo/x1/"));
        Assert.assertNull(uriParser.parseRequestUri("/foo/v/"));
        Assert.assertNull(uriParser.parseRequestUri("/foo/1/"));
    }

    private void assertResult(String endpoint, boolean audit, String id, String version, UriRequestResult result)
    {
        Assert.assertNotNull(result);
        Assert.assertEquals(endpoint,result.getEndpoint());
        Assert.assertEquals(audit,result.isHistory());
        Assert.assertEquals(id,result.getId());
        Assert.assertEquals(version,result.getVersion());
    }
}
