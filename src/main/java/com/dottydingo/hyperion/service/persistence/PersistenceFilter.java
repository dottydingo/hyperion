package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.service.context.PersistenceContext;
import com.dottydingo.hyperion.service.persistence.query.PredicateBuilder;

/**
 * User: mark
 * Date: 10/7/12
 * Time: 2:35 PM
 */
public interface PersistenceFilter<P>
{
    PredicateBuilder<P> getFilterPredicateBuilder(PersistenceContext persistenceContext);

    boolean isVisible(P persistentObject,PersistenceContext persistenceContext);

    boolean canCreate(P persistentObject, PersistenceContext persistenceContext);

    boolean canUpdate(P persistentObject, PersistenceContext persistenceContext);

    boolean canDelete(P persistentObject, PersistenceContext persistenceContext);
}
