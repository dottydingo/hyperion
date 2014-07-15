package com.dottydingo.hyperion.core.persistence.history;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.exception.InternalException;
import com.dottydingo.hyperion.core.endpoint.pipeline.auth.AuthorizationContext;;
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
            // no easy way to localize, though this shouldn't generally happen in a production context
            throw new InternalException("Error creating API instance.",e);
        }
    }
}
