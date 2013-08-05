package com.dottydingo.hyperion.service.context;

import junit.framework.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 */
public class HyperionContextBuilderTest
{
    private HyperionContextBuilder contextBuilder = new HyperionContextBuilder();

    @Test
    public void testGetRequestUri()
    {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContextPath("/foo");
        request.setRequestURI("/foo/bar/baz");

        Assert.assertEquals("/bar/baz", contextBuilder.getResourceUri(request));
    }
}
