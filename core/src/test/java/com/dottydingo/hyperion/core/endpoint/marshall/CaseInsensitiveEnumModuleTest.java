package com.dottydingo.hyperion.core.endpoint.marshall;

import com.dottydingo.hyperion.api.exception.InternalException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CaseInsensitiveEnumModuleTest
{
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception
    {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new CaseInsensitiveEnumModule());
    }

    @Test
    public void testMatch() throws Exception
    {
        testValue("foo",CaseEnum.foo);
        testValue("Foo",CaseEnum.foo);
        testValue("BAR",CaseEnum.bar);
    }

    @Test(expected = InvalidFormatException.class)
    public void testNoMatch() throws Exception
    {
        testValue("fooz",null);
    }

    @Test(expected = InternalException.class)
    public void testInvalidEnum() throws Exception
    {
        objectMapper.readValue(String.format("{\"dupEnum\":\"%s\"}", "foo"), DupPojo.class);
    }

    private void testValue(String value,CaseEnum expected) throws Exception
    {
        Pojo pojo = objectMapper.readValue(String.format("{\"caseEnum\":\"%s\"}",value),Pojo.class);
        assertNotNull(pojo);
        assertEquals(expected,pojo.getCaseEnum());
    }
}