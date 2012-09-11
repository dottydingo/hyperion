package com.dottydingo.hyperion.service.endpoint;

import com.dottydingo.hyperion.api.ApiObject;

import java.io.Serializable;

/**
 */
public class EntityRequest<T extends ApiObject>
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
