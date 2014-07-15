package com.dottydingo.hyperion.core.persistence;


import com.dottydingo.hyperion.core.persistence.query.PersistentQueryBuilder;

/**
 * User: mark
 * Date: 10/7/12
 * Time: 2:35 PM
 */
public interface PersistenceFilter<P>
{
    PersistentQueryBuilder getFilterQueryBuilder(PersistenceContext persistenceContext);

    boolean isVisible(P persistentObject,PersistenceContext persistenceContext);

    boolean canCreate(P persistentObject, PersistenceContext persistenceContext);

    boolean canUpdate(P persistentObject, PersistenceContext persistenceContext);

    boolean canDelete(P persistentObject, PersistenceContext persistenceContext);
}
