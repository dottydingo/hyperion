package com.dottydingo.hyperion.core.persistence.query;

import com.dottydingo.hyperion.api.exception.BadParameterException;
import com.dottydingo.hyperion.core.StubMessageSource;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

public class DefaultArgumentParserTest
{
    private PersistenceContext context;
    private DefaultArgumentParser parser = new DefaultArgumentParser();

    @Before
    public void init()
    {
        context = new PersistenceContext();
        context.setLocale(Locale.US);
        context.setMessageSource(new StubMessageSource());
    }

    @Test
    public void testParse_basicTypes() throws Exception
    {
        assertEquals("test",parser.parse("test",String.class,context));
        assertEquals(new Integer(123),parser.parse("123",Integer.class,context));
        assertEquals(new Integer(123),parser.parse("123",int.class,context));

        assertEquals(Boolean.TRUE,parser.parse("true",Boolean.class,context));
        assertEquals(Boolean.TRUE,parser.parse("true",boolean.class,context));

        assertEquals(new Float(123.1),parser.parse("123.1",Float.class,context));
        assertEquals(new Float(123.1),parser.parse("123.1",float.class,context));

        assertEquals(new Double(123.1),parser.parse("123.1",Double.class,context));
        assertEquals(new Double(123.1),parser.parse("123.1",double.class,context));

        assertEquals(new Long(123),parser.parse("123",Long.class,context));
        assertEquals(new Long(123),parser.parse("123",long.class,context));
    }

    @Test
    public void testParse_enum() throws Exception
    {
        assertEquals(SampleEnum.Mixed,parser.parse("Mixed",SampleEnum.class,context));
        assertEquals(SampleEnum.UPPER,parser.parse("upper",SampleEnum.class,context));
        assertEquals(SampleEnum.lower,parser.parse("LOWER",SampleEnum.class,context));

        try
        {
            parser.parse("foo",SampleEnum.class,context);
            fail();
        }
        catch (BadParameterException ignore){}
    }

    @Test
    public void testParse_date() throws Exception
    {
        assertEquals(getDate("2015-01-01"),parser.parse("2015-01-01",Date.class,context));
        assertEquals(getDate("2015-01-01T10:40:00"),parser.parse("2015-01-01T10:40:00",Date.class,context));
        assertEquals(getDate("2015-01-01T10:40:00.100"),parser.parse("2015-01-01T10:40:00.100",Date.class,context));
        assertEquals(getDate("2015-01-01T10:40:00.100-07:00"),parser.parse("2015-01-01T10:40:00.100-07:00",Date.class,context));
    }

    @Test
    public void testParse_valueOf() throws Exception
    {
        assertEquals(new SampleValueOf("testme"),parser.parse("testme",SampleValueOf.class,context));
    }

    private Date getDate(String value)
    {
        return ISODateTimeFormat.dateOptionalTimeParser().parseDateTime(value).toDate();
    }
}