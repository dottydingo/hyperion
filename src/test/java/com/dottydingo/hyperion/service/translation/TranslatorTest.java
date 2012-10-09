package com.dottydingo.hyperion.service.translation;

import com.dottydingo.hyperion.service.context.RequestContext;
import com.dottydingo.hyperion.service.context.RequestContextImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * User: mark
 * Date: 9/3/12
 * Time: 9:56 PM
 */
public class TranslatorTest
{
    private BaseTranslator<SimpleClientObject,SimplePersistentObject> translator;


    @Before
    public void setup()
    {
        translator = new BaseTranslator<SimpleClientObject,SimplePersistentObject>(SimpleClientObject.class,
                SimplePersistentObject.class);
        translator.init();
    }

    @Test
    public void testConvertClient()
    {
        SimpleClientObject clientObject = new SimpleClientObject();
        clientObject.setId(1L);
        clientObject.setName("name");
        clientObject.setNumber(100);
        clientObject.setDifferentType("Should not get copied by default");
        clientObject.setClientOnly("No field");

        RequestContext context = new RequestContextImpl();

        SimplePersistentObject persistentObject = translator.convertClient(clientObject,context);

        Assert.assertNotNull(persistentObject);
        Assert.assertEquals(new Long(1),persistentObject.getId());
        Assert.assertEquals("name",persistentObject.getName());
        Assert.assertEquals(new Integer(100),persistentObject.getNumber());
        Assert.assertNull(persistentObject.getDifferentType());
        Assert.assertNull(persistentObject.getPersistentOnly());
    }

    @Test
    public void testCopyClient()
    {
        SimpleClientObject clientObject = new SimpleClientObject();
        clientObject.setId(10L);
        clientObject.setName("name");
        clientObject.setNumber(100);
        clientObject.setDifferentType("Should not get copied by default");
        clientObject.setClientOnly("No field");

        SimplePersistentObject persistentObject = new SimplePersistentObject();
        persistentObject.setId(50L);
        persistentObject.setName("should get overwritten");
        persistentObject.setNumber(5);
        persistentObject.setDifferentType(88);
        persistentObject.setPersistentOnly("Should not be touched");

        RequestContext context = new RequestContextImpl();

        translator.copyClient(clientObject, persistentObject, context);

        Assert.assertNotNull(persistentObject);
        Assert.assertEquals(new Long(10),persistentObject.getId());
        Assert.assertEquals("name",persistentObject.getName());
        Assert.assertEquals(new Integer(100),persistentObject.getNumber());
        Assert.assertEquals(new Integer(88),persistentObject.getDifferentType());
        Assert.assertEquals("Should not be touched",persistentObject.getPersistentOnly());
    }

    @Test
    public void testCopyClient_Sparse()
    {
        SimpleClientObject clientObject = new SimpleClientObject();
        clientObject.setId(10L);
        clientObject.setName("name");

        SimplePersistentObject persistentObject = new SimplePersistentObject();
        persistentObject.setId(50L);
        persistentObject.setName("should get overwritten");
        persistentObject.setNumber(5);
        persistentObject.setDifferentType(88);
        persistentObject.setPersistentOnly("Should not be touched");

        RequestContext context = new RequestContextImpl();

        translator.copyClient(clientObject, persistentObject, context);

        Assert.assertNotNull(persistentObject);
        Assert.assertEquals(new Long(10),persistentObject.getId());
        Assert.assertEquals("name",persistentObject.getName());
        Assert.assertEquals(new Integer(5),persistentObject.getNumber());
        Assert.assertEquals(new Integer(88),persistentObject.getDifferentType());
        Assert.assertEquals("Should not be touched",persistentObject.getPersistentOnly());
    }

    @Test
    public void testConvertPersistent()
    {
        new SimpleClientObject();

        SimplePersistentObject persistentObject = new SimplePersistentObject();
        persistentObject.setId(50L);
        persistentObject.setName("name");
        persistentObject.setNumber(5);
        persistentObject.setDifferentType(88);
        persistentObject.setPersistentOnly("Should not be copied");

        RequestContext context = new RequestContextImpl();

        SimpleClientObject clientObject = translator.convertPersistent(persistentObject, context);

        Assert.assertNotNull(clientObject);
        Assert.assertEquals(new Long(50),persistentObject.getId());
        Assert.assertEquals("name",clientObject.getName());
        Assert.assertEquals(new Integer(5),clientObject.getNumber());
        Assert.assertNull(clientObject.getDifferentType());
        Assert.assertNull(clientObject.getClientOnly());
    }
}
