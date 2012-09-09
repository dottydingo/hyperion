package com.dottydingo.hyperion.api;

import java.io.Serializable;

/**
 * Base API Object
 */
public class BaseApiObject<ID extends Serializable>
{
    private ID id;

    public ID getId()
    {
        return id;
    }

    public void setId(ID id)
    {
        this.id = id;
    }
}
