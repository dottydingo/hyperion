package com.dottydingo.hyperion.core.translation;

import com.fasterxml.classmate.MemberResolver;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.ResolvedTypeWithMembers;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.ResolvedField;
import net.sf.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: mark
 * Date: 11/26/12
 * Time: 6:36 PM
 */
public class TypeMapper
{
    private Map<String,ResolvedField> fields = new HashMap<String, ResolvedField>();
    private BeanMap beanMap;

    public TypeMapper(Class type)
    {
        TypeResolver typeResolver = new TypeResolver();
        ResolvedType resolvedType = typeResolver.resolve(type);

        MemberResolver memberResolver = new MemberResolver(typeResolver);
        ResolvedTypeWithMembers typeWithMembers =memberResolver.resolve(resolvedType, null, null);

        for (ResolvedField field : typeWithMembers.getMemberFields())
        {
            fields.put(field.getName(),field);
        }

        beanMap = BeanMap.create(createInstance(type));
    }

    public Class getFieldType(String fieldName)
    {
        ResolvedField field = fields.get(fieldName);
        if(field == null) return null;

        return field.getType().getErasedType();
    }

    public Object getValue(Object bean,String field)
    {
        return beanMap.get(bean,field);
    }

    public void setValue(Object bean, String property, Object value)
    {
        beanMap.put(bean,property,value);
    }

    public Set<String> getFieldNames()
    {
        return fields.keySet();
    }

    private Object createInstance(Class type)
    {
        try
        {
            return type.newInstance();
        }
        catch (Exception e)
        {
            throw new RuntimeException(
                    String.format("Error creating instance of %s",type.getName()),e);
        }

    }
}
