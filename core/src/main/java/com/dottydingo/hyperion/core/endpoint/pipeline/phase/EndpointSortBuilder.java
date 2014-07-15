package com.dottydingo.hyperion.core.endpoint.pipeline.phase;

import com.dottydingo.hyperion.core.endpoint.EndpointSort;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;

/**
 */
public interface EndpointSortBuilder
{
    EndpointSort buildSort(String sortString, PersistenceContext persistenceContext);
}
