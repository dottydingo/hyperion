package com.dottydingo.hyperion.core.translation;

import com.dottydingo.hyperion.core.endpoint.pipeline.auth.NoOpAuthorizationContext;
import com.dottydingo.hyperion.core.model.PersistentObject;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.hyperion.core.endpoint.pipeline.auth.AuthorizationContext;
import com.dottydingo.hyperion.core.registry.EntityPlugin;
import com.dottydingo.service.endpoint.context.UserContext;
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
    private PersistenceContext context;

    @Before
    public void setup()
    {
        translator = new DefaultTranslator<SimpleClientObject,SimplePersistentObject>(SimpleClientObject.class,
                SimplePersistentObject.class);
        translator.init();
        context = new PersistenceContext();
        context.setAuthorizationContext(new NoOpAuthorizationContext(null));
        context.setEntityPlugin(new EntityPlugin());
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

        SimpleClientObject clientObject = translator.convertPersistent(persistentObject, context);

        Assert.assertNotNull(clientObject);
        Assert.assertEquals(new Long(50),persistentObject.getId());
        Assert.assertEquals("name",clientObject.getName());
        Assert.assertEquals(new Integer(5),clientObject.getNumber());
        Assert.assertNull(clientObject.getDifferentType());
        Assert.assertNull(clientObject.getClientOnly());
    }
}
