package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.service.persistence.query.PredicateBuilder;

/**
 * User: mark
 * Date: 10/7/12
 * Time: 2:54 PM
 */
public class EmptyPersistenceFilter<P> implements PersistenceFilter<P>
{
    @Override
    public PredicateBuilder<P> getFilterPredicateBuilder(PersistenceContext persistenceContext)
    {
        return null;
    }

    @Override
    public boolean isVisible(P persistentObject, PersistenceContext persistenceContext)
    {
        return true;
    }

    @Override
    public boolean canCreate(P persistentObject, PersistenceContext persistenceContext)
    {
        return true;
    }

    @Override
    public boolean canUpdate(P persistentObject, PersistenceContext persistenceContext)
    {
        return true;
    }

    @Override
    public boolean canDelete(P persistentObject, PersistenceContext persistenceContext)
    {
        return true;
    }
}
