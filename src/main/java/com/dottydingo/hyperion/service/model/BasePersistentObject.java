package com.dottydingo.hyperion.service.model;


import javax.persistence.*;
import java.io.Serializable;

/**
 * An abstract base class that can be used for persistent objects.
 * @param <ID>
 */
@MappedSuperclass
public abstract class BasePersistentObject<ID extends Serializable> implements PersistentObject<ID>
{
    /**
     * A flag indicating if an id has been set (for compatibility with Spring JPA Persistable)
     * @return true if the ID has been set, false otherwise.
     */
    public boolean isNew()
    {
        return getId() == null;
    }

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
