package com.dottydingo.hyperion.service.translation;

import com.dottydingo.hyperion.service.persistence.PersistenceContext;

/**
 */
public class DefaultFieldMapper <C,P> implements FieldMapper<C,P>
{
    private String clientFieldName;
    private String persistentFieldName;
    protected ValueConverter valueConverter;
    protected PropertyChangeEvaluator propertyChangeEvaluator = new DefaultPropertyChangeEvaluator();

    public DefaultFieldMapper(String name)
    {
        this(name,name,null);
    }

    public DefaultFieldMapper(String clientFieldName, String persistentFieldName,ValueConverter valueConverter)
    {
        this.clientFieldName = clientFieldName;
        this.persistentFieldName = persistentFieldName;
        this.valueConverter = valueConverter;
    }

    @Override
    public String getClientFieldName()
    {
        return clientFieldName;
    }

    public String getPersistentFieldName()
    {
        return persistentFieldName;
    }

    public void setPropertyChangeEvaluator(PropertyChangeEvaluator propertyChangeEvaluator)
    {
        this.propertyChangeEvaluator = propertyChangeEvaluator;
    }

    @Override
    public void convertToClient(ObjectWrapper<P> persistentObjectWrapper,
                                ObjectWrapper<C> clientObjectWrapper, PersistenceContext context)
    {
        Object persistentValue = persistentObjectWrapper.getValue(getPersistentFieldName());

        if(valueConverter != null)
        {
            persistentValue = valueConverter.convertToClientValue(persistentValue,context);
        }

        clientObjectWrapper.setValue(getClientFieldName(), persistentValue);
    }

    @Override
    public boolean convertToPersistent(ObjectWrapper<C> clientObjectWrapper,
                                    ObjectWrapper<P> persistentObjectWrapper, PersistenceContext context)
    {
        Object clientValue = clientObjectWrapper.getValue(getClientFieldName());
        if(valueConverter != null)
        {
            clientValue = valueConverter.convertToPersistentValue(clientValue,context);
        }

        boolean dirty = false;

        if(clientValue != null || context.isFieldProvided(getClientFieldName()))
        {
            dirty = propertyChangeEvaluator.hasChanged(persistentObjectWrapper.getValue(persistentFieldName),
                    clientValue);
            if(dirty)
                persistentObjectWrapper.setValue(getPersistentFieldName(), clientValue);
        }

        return dirty;
    }
}
