package com.dottydingo.hyperion.core.persistence;


import com.dottydingo.hyperion.core.persistence.query.PersistentQueryBuilder;

/**
 * No-op implementation allowing all access.
 */
public class EmptyPersistenceFilter<P> implements PersistenceFilter<P>
{
    @Override
    public PersistentQueryBuilder getFilterQueryBuilder(PersistenceContext persistenceContext)
    {
        return null;
    }

    @Override
    public boolean isVisible(P persistentObject, PersistenceContext persistenceContext)
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
