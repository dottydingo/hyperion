package com.dottydingo.hyperion.service.translation;

import net.sf.cglib.beans.BeanMap;
import org.junit.Assert;
import org.junit.Test;

/**
 * User: mark
 * Date: 9/3/12
 * Time: 9:57 PM
 */
public class DefaultFieldMapperTest
{
    private static final BeanMap clientBeanMap = BeanMap.create(new SimpleClientObject());
    private static final BeanMap persistentBeanMap = BeanMap.create(new SimplePersistentObject());

    @Test
    public void testSameNameMapping()
    {
        DefaultFieldMapper<SimpleClientObject,SimplePersistentObject> mapper =
                new DefaultFieldMapper<SimpleClientObject, SimplePersistentObject>("name",clientBeanMap,persistentBeanMap);

        SimpleClientObject client = new SimpleClientObject();
        client.setName("should be overwritten");

        SimplePersistentObject persistent = new SimplePersistentObject();
        persistent.setName("happy new value");

        DefaultTranslationContext context = new DefaultTranslationContext();
        mapper.convertToClient(persistent,client, context);

        Assert.assertEquals("happy new value",client.getName());

        persistent.setName("Overwrite me");
        client.setName("new value");

        mapper.convertToPersistent(client,persistent,context);
        Assert.assertEquals("new value",persistent.getName());
    }

    @Test
    public void testDifferentNameMapping()
    {
        DefaultFieldMapper<SimpleClientObject,SimplePersistentObject> mapper =
                new DefaultFieldMapper<SimpleClientObject, SimplePersistentObject>("clientOnly","persistentOnly",
                        clientBeanMap,persistentBeanMap,null);

        SimpleClientObject client = new SimpleClientObject();
        client.setClientOnly("should be overwritten");

        SimplePersistentObject persistent = new SimplePersistentObject();
        persistent.setPersistentOnly("happy new value");

        DefaultTranslationContext context = new DefaultTranslationContext();
        mapper.convertToClient(persistent,client, context);

        Assert.assertEquals("happy new value",client.getClientOnly());

        persistent.setPersistentOnly("Overwrite me");
        client.setClientOnly("new value");

        mapper.convertToPersistent(client,persistent,context);
        Assert.assertEquals("new value",persistent.getPersistentOnly());
    }

    @Test
    public void testWithConverter()
    {
        DefaultFieldMapper<SimpleClientObject,SimplePersistentObject> mapper =
                new DefaultFieldMapper<SimpleClientObject, SimplePersistentObject>("differentType","differentType",
                        clientBeanMap,persistentBeanMap,new StringConverter());

        SimpleClientObject client = new SimpleClientObject();
        client.setDifferentType("42");

        SimplePersistentObject persistent = new SimplePersistentObject();
        persistent.setDifferentType(22);

        DefaultTranslationContext context = new DefaultTranslationContext();
        mapper.convertToClient(persistent,client, context);

        Assert.assertEquals("22",client.getDifferentType());

        persistent.setDifferentType(99);
        client.setDifferentType("4");

        mapper.convertToPersistent(client,persistent,context);
        Assert.assertEquals(new Integer(4), persistent.getDifferentType());
    }

    private class StringConverter implements ValueConverter<String,Integer>
    {
        @Override
        public String convertToClientValue(Integer persistentValue, TranslationContext context)
        {
            if(persistentValue == null)
                return null;

            return persistentValue.toString();
        }

        @Override
        public Integer convertToPersistentValue(String clientValue, TranslationContext context)
        {
            if(clientValue == null)
                return null;

            return Integer.parseInt(clientValue);
        }
    }
}
