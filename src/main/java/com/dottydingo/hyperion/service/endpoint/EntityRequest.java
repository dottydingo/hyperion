package com.dottydingo.hyperion.service.endpoint;

import com.dottydingo.hyperion.api.BaseApiObject;

import java.io.Serializable;

/**
 */
public class EntityRequest<T extends BaseApiObject>
{
    private T item;

    public T getItem()
    {
        return item;
    }

    public void setItem(T item)
    {
        this.item = item;
    }
}
