package com.dottydingo.hyperion.core.persistence.sort;

import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.hyperion.core.registry.EntityPlugin;
import com.dottydingo.hyperion.core.endpoint.EndpointSort;

/**
 * User: mark
 * Date: 2/24/13
 * Time: 10:11 AM
 */
public interface PersistentOrderBuilderFactory<B extends PersistentOrderBuilder>
{
    B createOrderBuilder(EndpointSort endpointSort, PersistenceContext persistenceContext);
}
