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

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof BaseApiObject))
        {
            return false;
        }

        BaseApiObject that = (BaseApiObject) o;

        if (id != null ? !id.equals(that.id) : that.id != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return id != null ? id.hashCode() : 0;
    }
}
