package com.dottydingo.hyperion.core.translation;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

/**
 */
public class TypeMapperTest
{
    @Test
    public void testTypes()
    {
        TypeMapper typeMapper = new TypeMapper(SimpleClientObject.class);
        Assert.assertEquals(Long.class, typeMapper.getFieldType("id"));
        Assert.assertEquals(String.class, typeMapper.getFieldType("name"));
        Assert.assertEquals(Integer.class, typeMapper.getFieldType("number"));
        Assert.assertEquals(String.class, typeMapper.getFieldType("clientOnly"));
        Assert.assertEquals(String.class, typeMapper.getFieldType("differentType"));

    }

    @Test
    public void testGetFieldNames()
    {
        TypeMapper typeMapper = new TypeMapper(SimpleClientObject.class);
        Set<String> fieldNames = typeMapper.getFieldNames();
        Assert.assertEquals(5,fieldNames.size());
        Assert.assertTrue(fieldNames.contains("id"));
        Assert.assertTrue(fieldNames.contains("name"));
        Assert.assertTrue(fieldNames.contains("number"));
        Assert.assertTrue(fieldNames.contains("clientOnly"));
        Assert.assertTrue(fieldNames.contains("differentType"));
    }

    @Test
    public void testGetValue()
    {
        TypeMapper typeMapper = new TypeMapper(SimpleClientObject.class);
        SimpleClientObject clientObject = new SimpleClientObject();
        clientObject.setId(1L);
        clientObject.setName("name");
        clientObject.setNumber(99);
        clientObject.setClientOnly("client");
        clientObject.setDifferentType("differentType");

        Assert.assertEquals(1L,typeMapper.getValue(clientObject,"id"));
        Assert.assertEquals("name",typeMapper.getValue(clientObject,"name"));
        Assert.assertEquals(99,typeMapper.getValue(clientObject,"number"));
        Assert.assertEquals("client",typeMapper.getValue(clientObject,"clientOnly"));
        Assert.assertEquals("differentType",typeMapper.getValue(clientObject,"differentType"));
    }

    @Test
    public void testSetValue()
    {
        TypeMapper typeMapper = new TypeMapper(SimpleClientObject.class);
        SimpleClientObject clientObject = new SimpleClientObject();
        typeMapper.setValue(clientObject,"id",2L);
        typeMapper.setValue(clientObject,"name","name");
        typeMapper.setValue(clientObject,"number",42);
        typeMapper.setValue(clientObject,"clientOnly","client");
        typeMapper.setValue(clientObject,"differentType","type");

        Assert.assertEquals(new Long(2),clientObject.getId());
        Assert.assertEquals("name",clientObject.getName());
        Assert.assertEquals(new Integer(42),clientObject.getNumber());
        Assert.assertEquals("client",clientObject.getClientOnly());
        Assert.assertEquals("type",clientObject.getDifferentType());
    }

}
