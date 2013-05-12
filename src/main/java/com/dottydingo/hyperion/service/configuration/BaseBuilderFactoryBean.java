package com.dottydingo.hyperion.service.configuration;

import com.dottydingo.hyperion.service.persistence.sort.SortBuilder;
import com.fasterxml.classmate.MemberResolver;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.ResolvedTypeWithMembers;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.ResolvedField;
import org.springframework.beans.factory.FactoryBean;

import java.util.*;

/**
 */
public abstract class BaseBuilderFactoryBean<T> implements FactoryBean<Map<String,T>>
{
    protected Class<?> apiClass;
    protected Map<String,T> overrides = new HashMap<String, T>();
    protected Set<String> excludeFields = new HashSet<String>();
    private Map<String,String> fieldNameRemapping = new HashMap<String, String>();

    public void setApiClass(Class<?> apiClass)
    {
        this.apiClass = apiClass;
    }

    public void setFieldNameRemapping(Map<String, String> fieldNameRemapping)
    {
        this.fieldNameRemapping = fieldNameRemapping;
    }

    public void setOverrides(Map<String, T> overrides)
    {
        this.overrides = overrides;
    }

    public void setExcludeFields(Set<String> excludeFields)
    {
        this.excludeFields = excludeFields;
    }

    protected String getResolvedName(String name)
    {
        if(fieldNameRemapping.containsKey(name))
            return fieldNameRemapping.get(name);

        return name;
    }

    protected List<String> getFieldNames(Class<?> type)
    {
        List<String> fields = new ArrayList<String>();
        TypeResolver typeResolver = new TypeResolver();
        ResolvedType resolvedType = typeResolver.resolve(type);

        MemberResolver memberResolver = new MemberResolver(typeResolver);
        ResolvedTypeWithMembers typeWithMembers = memberResolver.resolve(resolvedType, null, null);

        for (ResolvedField field : typeWithMembers.getMemberFields())
        {
            fields.add(field.getName());
        }

        return fields;
    }

    @Override
    public Map<String, T> getObject() throws Exception
    {
        Map<String,T> map = new HashMap<String, T>();
        List<String> fields = getFieldNames(apiClass);
        for (String field : fields)
        {
            if(excludeFields.contains(field))
                continue;

            T builder = overrides.get(field);
            if(builder == null)
                builder = createBuilder(field);

            String resolved = getResolvedName(field);

            map.put(resolved,builder);
        }

        return map;
    }

    protected abstract T createBuilder(String fieldName);

    @Override
    public Class<?> getObjectType()
    {
        return Map.class;
    }

    @Override
    public boolean isSingleton()
    {
        return true;
    }
}
