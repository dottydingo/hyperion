package com.dottydingo.hyperion.core.persistence.event;

import com.dottydingo.hyperion.api.ApiObject;

import java.io.Serializable;


/**
 * A listener for handling change events after the transaction has been commited
 */
public interface EntityChangeListener<C extends ApiObject,ID extends Serializable>
{
    void processEntityChange(EntityChangeEvent<C,ID> event);
}
