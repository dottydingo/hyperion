package com.dottydingo.hyperion.core.persistence.event;

import com.dottydingo.hyperion.api.ApiObject;

import java.io.Serializable;

/**
 * A listener for events that are to be handled within the transaction
 */
public interface PersistentChangeListener<C extends ApiObject,ID extends Serializable>
{
    void processEntityChange(PersistentChangeEvent<C, ID> event);
}
