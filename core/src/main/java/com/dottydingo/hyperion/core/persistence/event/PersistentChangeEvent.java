package com.dottydingo.hyperion.core.persistence.event;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;

import java.io.Serializable;
import java.util.Set;

/**
 * Events that can be handled in the transaction layer.
 *
 */
public class PersistentChangeEvent<C extends ApiObject,ID extends Serializable> extends EntityChangeEvent<C,ID>
{

    public PersistentChangeEvent(C originalItem, C updatedItem, Set<String> updatedFields,
                                 PersistenceContext persistenceContext, ID id, EntityChangeAction entityChangeAction,
                                 String entity)
    {
        super(originalItem, updatedItem, updatedFields, persistenceContext, id, entityChangeAction, entity);
    }

}
