package com.dottydingo.hyperion.core.persistence;

import java.util.Objects;

/**
 */
public class ItemChangeKey
{
    private String endpointType;
    private Object id;

    public ItemChangeKey(String endpointType, Object id)
    {
        this.endpointType = endpointType;
        this.id = id;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof ItemChangeKey))
        {
            return false;
        }
        ItemChangeKey that = (ItemChangeKey) o;
        return Objects.equals(endpointType, that.endpointType) &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(endpointType, id);
    }
}
