package com.dottydingo.hyperion.core.persistence.event;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.core.endpoint.HttpMethod;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;

import java.io.Serializable;
import java.util.Set;

/**
 * An event triggered by a change to an entity
 */
public class EntityChangeEvent<C extends ApiObject>
{
    private C originalItem;
    private C updatedItem;
    private Set<String> updatedFields;
    private PersistenceContext persistenceContext;

    public EntityChangeEvent(C originalItem, C updatedItem, Set<String> updatedFields,
                             PersistenceContext persistenceContext)
    {
        this.originalItem = originalItem;
        this.updatedItem = updatedItem;
        this.updatedFields = updatedFields;
        this.persistenceContext = persistenceContext;
    }


    public C getOriginalItem()
    {
        return originalItem;
    }

    public C getUpdatedItem()
    {
        return updatedItem;
    }

    public Set<String> getUpdatedFields()
    {
        return updatedFields;
    }

    public PersistenceContext getPersistenceContext()
    {
        return persistenceContext;
    }

}
