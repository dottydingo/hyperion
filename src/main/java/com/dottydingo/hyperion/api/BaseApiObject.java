package com.dottydingo.hyperion.api;

import java.io.Serializable;

/**
 * Base API Object
 */
public class BaseApiObject<ID extends Serializable> implements ApiObject<ID>
{
    private ID id;

    /**
     * {@inheritDoc}
     */
    @Override
    public ID getId()
    {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setId(ID id)
    {
        this.id = id;
    }
}
