package com.dottydingo.hyperion.jpa.model;


import com.dottydingo.hyperion.core.model.PersistentObject;

import javax.persistence.*;
import java.io.Serializable;

/**
 * An abstract base class that can be used for persistent objects.
 * @param <ID> serializable identifier
 */
@MappedSuperclass
public abstract class BasePersistentObject<ID extends Serializable> implements PersistentObject<ID>
{

    public String toString() {

        return String.format("Entity of type %s with id: %s", this.getClass().getName(), getId());
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || !(o instanceof BasePersistentObject)) return false;

        BasePersistentObject that = (BasePersistentObject) o;

        if (getId() != null
                ? !getId().equals(that.getId())
                : that.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        return getId() != null
                ? getId().hashCode()
                : 0;
    }
}
