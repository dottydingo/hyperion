package com.dottydingo.hyperion.core.persistence.event;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.core.endpoint.HttpMethod;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;

import java.io.Serializable;
import java.util.Set;

/**
 * An event triggered by a change to an entity
 *
 * This class will be removed in an upcoming release
 */
public class EntityChangeEvent<C extends ApiObject,ID extends Serializable>
{
    protected ID id;
    protected EntityChangeAction entityChangeAction;
    protected String entity;
    private C originalItem;
    private C updatedItem;
    private Set<String> updatedFields;
    private PersistenceContext persistenceContext;

    public EntityChangeEvent(C originalItem, C updatedItem, Set<String> updatedFields,
                             PersistenceContext persistenceContext, ID id, EntityChangeAction entityChangeAction, String entity)
    {
        this.originalItem = originalItem;
        this.updatedItem = updatedItem;
        this.updatedFields = updatedFields;
        this.persistenceContext = persistenceContext;
        this.id = id;
        this.entityChangeAction = entityChangeAction;
        this.entity = entity;
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
}
