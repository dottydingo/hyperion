package com.dottydingo.hyperion.service.translation;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.service.model.PersistentObject;
import net.sf.cglib.reflect.FastClass;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class DefaultTranslator<C extends ApiObject,P extends PersistentObject> extends BaseTranslator<C,P>
{
    private FastClass clientClass;
    private FastClass persistentClass;
    private List<FieldMapper> fieldMappers = new ArrayList<FieldMapper>();

    public DefaultTranslator(Class<C> clientClass, Class<P> persistentClass)
    {
        this.clientClass = FastClass.create(clientClass);
        this.persistentClass = FastClass.create(persistentClass);
    }

    public void setFieldMappers(List<FieldMapper> fieldMappers)
    {
        this.fieldMappers = fieldMappers;
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

    @Override
    protected List<FieldMapper> getCustomFieldMappers()
    {
        List<FieldMapper> mappers = new ArrayList<FieldMapper>();
        mappers.addAll(super.getCustomFieldMappers());
        mappers.addAll(fieldMappers);
        return mappers;
    }
}
