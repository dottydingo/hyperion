package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.exception.InternalException;
import com.dottydingo.hyperion.service.pipeline.auth.AuthorizationContext;;
import net.sf.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 */
public class SimpleBeanFilter
{
    private Map<Class,BeanMap> beanMapMap = new HashMap<Class, BeanMap>();

    public <C extends ApiObject> C copy(C original, AuthorizationContext authorizationContext)
    {
        C copy = (C) createInstance(original.getClass());
        BeanMap beanMap = getBeanMap(copy);
        Set<String> set = beanMap.keySet();
        for (String propertyName : set)
        {
            if(authorizationContext.isReadable(propertyName))
                beanMap.put(copy,propertyName,beanMap.get(original,propertyName));
        }

        return copy;
    }

    private synchronized <C extends ApiObject> BeanMap getBeanMap(C object)
    {
        BeanMap beanMap = beanMapMap.get(object.getClass());
        if(beanMap == null)
        {
            beanMap = BeanMap.create(object);
            beanMapMap.put(object.getClass(),beanMap);
        }

        return beanMap;
    }

    private <C extends ApiObject> C createInstance(Class<C> type)
    {
        try
        {
            return type.newInstance();
        }
        catch (Exception e)
        {
            throw new InternalException("Error creating API instance.",e);
        }
    }
}
