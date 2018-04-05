package com.dottydingo.hyperion.core.persistence.event;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;

import java.io.Serializable;
import java.util.Set;

/**
 * Events that can be handled in the transaction layer.
 *
 */
public class PersistentChangeEvent<C extends ApiObject,ID extends Serializable>
{
    private ID id;
    private EntityChangeAction entityChangeAction;
    private String entity;
    private C originalItem;
    private C updatedItem;
    private Set<String> updatedFields;
    private PersistenceContext persistenceContext;

    public PersistentChangeEvent(C originalItem, C updatedItem, Set<String> updatedFields,
                                 PersistenceContext persistenceContext, ID id, EntityChangeAction entityChangeAction,
                                 String entity)
    {
        this.originalItem = originalItem;
        this.updatedItem = updatedItem;
        this.updatedFields = updatedFields;
        this.persistenceContext = persistenceContext;
        this.id = id;
        this.entityChangeAction = entityChangeAction;
        this.entity = entity;
    }

    public ID getId()
    {
        return id;
    }

    public EntityChangeAction getEntityChangeAction()
    {
        return entityChangeAction;
    }

    public String getEntity()
    {
        return entity;
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
