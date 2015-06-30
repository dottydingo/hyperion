package com.dottydingo.hyperion.core.translation;

import com.dottydingo.hyperion.core.persistence.PersistenceContext;

import java.io.Serializable;

/**
 */
public class DefaultIdFieldMapper<C,P> extends DefaultFieldMapper<C,P> implements IdFieldMapper<C,P>
{
    public DefaultIdFieldMapper()
    {
        super("id");
    }

    public DefaultIdFieldMapper(ValueConverter valueConverter)
    {
        super("id", "id", valueConverter);
    }

    @Override
    public <ID extends Serializable> ID convertId(ObjectWrapper<C> client, PersistenceContext context)
    {
        Object value = client.getValue("id");
        if(valueConverter != null)
            value = valueConverter.convertToClientValue(value,context);

        return (ID) value;
    }
}
