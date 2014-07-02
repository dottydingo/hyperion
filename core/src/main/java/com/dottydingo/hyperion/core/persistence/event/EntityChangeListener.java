package com.dottydingo.hyperion.core.persistence.event;

import com.dottydingo.hyperion.api.ApiObject;


/**
 * A listener for handling change events after the transaction has been commited
 */
public interface EntityChangeListener<C extends ApiObject>
{
    void processEntityChange(EntityChangeEvent<C> event);
}
