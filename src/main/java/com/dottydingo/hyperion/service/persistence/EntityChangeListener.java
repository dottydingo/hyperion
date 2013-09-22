package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.api.ApiObject;

/**
 */
public interface EntityChangeListener<C extends ApiObject>
{
    void processEntityChange(EntityChangeEvent<C> event);
}
