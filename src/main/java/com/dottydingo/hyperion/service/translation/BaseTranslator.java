package com.dottydingo.hyperion.service.translation;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.service.context.RequestContext;
import com.dottydingo.hyperion.service.model.PersistentObject;
import net.sf.cglib.beans.BeanMap;
import net.sf.cglib.reflect.FastClass;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 */
public class BaseTranslator<C extends ApiObject,P extends PersistentObject> implements Translator<C,P>
{
    protected BeanMap clientBeanMap;
    protected BeanMap persistentBeanMap;
    private FastClass clientClass;
    private FastClass persistentClass;
    private Map<String,FieldMapper> fieldMapperMap = new HashMap<String, FieldMapper>();

    protected BaseTranslator(Class<C> clientClass, Class<P> persistentClass)
    {
        this.clientClass = FastClass.create(clientClass);
        this.persistentClass = FastClass.create(persistentClass);
    }

    protected C createClientInstance()
    {
        try
        {
            return (C) clientClass.newInstance();
        }
        catch (InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
    }
    protected P createPersistentInstance()
    {
        try
        {
            return (P) persistentClass.newInstance();
        }
        catch (InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void init()
    {
        clientBeanMap = BeanMap.create(createClientInstance());
        persistentBeanMap = BeanMap.create(createPersistentInstance());
        initializeDefaultFieldMappers();
        initializeCustomFieldMappers();
    }

    protected void beforeConvert(ObjectWrapper<C> clientObjectWrapper, ObjectWrapper<P> persistentObjectWrapper,
                                 RequestContext context){}

    @Override
    public P convertClient(C client, RequestContext context)
    {
        P persistentObject = createPersistentInstance();
        ObjectWrapper<P> persistentObjectWrapper = createPersistentObjectWrapper(persistentObject,context);
        ObjectWrapper<C> clientObjectWrapper = createClientObjectWrapper(client,context);

        beforeConvert(clientObjectWrapper,persistentObjectWrapper,context);

        for (FieldMapper mapper : fieldMapperMap.values())
        {
            mapper.convertToPersistent(clientObjectWrapper,persistentObjectWrapper,context);
        }

        afterConvert(clientObjectWrapper,persistentObjectWrapper,context);

        return persistentObject;
    }

    protected void afterConvert(ObjectWrapper<C> clientObjectWrapper, ObjectWrapper<P> persistentObjectWrapper,
                                RequestContext context){}

    protected void beforeCopy(ObjectWrapper<C> clientObjectWrapper, ObjectWrapper<P> persistentObjectWrapper,
                              RequestContext context){}

    @Override
    public void copyClient(C client, P persistent, RequestContext context)
    {
        ObjectWrapper<P> persistentObjectWrapper = createPersistentObjectWrapper(persistent,context);
        ObjectWrapper<C> clientObjectWrapper = createClientObjectWrapper(client,context);

        beforeCopy(clientObjectWrapper,persistentObjectWrapper,context);
        for (FieldMapper mapper : fieldMapperMap.values())
        {
            mapper.convertToPersistent(clientObjectWrapper,persistentObjectWrapper,context);
        }
        afterCopy(clientObjectWrapper,persistentObjectWrapper,context);
    }

    protected void afterCopy(ObjectWrapper<C> clientObjectWrapper, ObjectWrapper<P> persistentObjectWrapper,
                             RequestContext context){}

    @Override
    public C convertPersistent(P persistent, RequestContext context)
    {
        C clientObject = createClientInstance();

        ObjectWrapper<P> persistentObjectWrapper = createPersistentObjectWrapper(persistent,context);
        ObjectWrapper<C> clientObjectWrapper = createClientObjectWrapper(clientObject,context);

        Set<String> requestedFields = context.getRequestedFields();

        for (Map.Entry<String, FieldMapper> entry : fieldMapperMap.entrySet())
        {
            if(requestedFields == null || requestedFields.contains(entry.getKey()))
            {
                entry.getValue().convertToClient(persistentObjectWrapper,clientObjectWrapper,context);
            }
        }

        convertPersistent(clientObject,persistent,context);

        return clientObject;
    }

    protected void convertPersistent(C client, P persistent, RequestContext context){}

    @Override
    public List<C> convertPersistent(List<P> persistent, RequestContext context)
    {
        List<C> list = new LinkedList<C>();
        for (P p : persistent)
        {
            list.add(convertPersistent(p,context));
        }

        return list;
    }


    private void initializeDefaultFieldMappers()
    {
        Set<String> clientFields =  clientBeanMap.keySet();
        for (String clientField : clientFields)
        {
            Class clientType = clientBeanMap.getPropertyType(clientField);
            Class persistentType = persistentBeanMap.getPropertyType(clientField);
            if(persistentType != null && clientType.equals(persistentType))
            {
                fieldMapperMap.put(clientField,new DefaultFieldMapper(clientField));
            }
        }
    }

    private void initializeCustomFieldMappers()
    {
        List<FieldMapper> mappers = getCustomFieldMappers();
        for (FieldMapper mapper : mappers)
        {
            fieldMapperMap.put(mapper.getClientFieldName(),mapper);
        }
    }

    protected List<FieldMapper> getCustomFieldMappers()
    {
        return new ArrayList<FieldMapper>();
    }

    protected ObjectWrapper<C> createClientObjectWrapper(C client,  RequestContext context)
    {
        return new ObjectWrapper<C>(client,clientBeanMap);
    }

    protected ObjectWrapper<P> createPersistentObjectWrapper(P persistent, RequestContext context)
    {
        return new ObjectWrapper<P>(persistent,persistentBeanMap);
    }
}
