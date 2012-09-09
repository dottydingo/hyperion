package com.dottydingo.hyperion.service.model;


import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public abstract class BasePersistentObject<ID extends Serializable> implements Persistable<ID>
{
    @Override
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
        if (o == null || getClass() != o.getClass()) return false;

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
