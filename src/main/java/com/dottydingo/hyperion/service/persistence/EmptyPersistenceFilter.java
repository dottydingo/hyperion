package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.service.context.RequestContext;
import com.dottydingo.hyperion.service.persistence.query.PredicateBuilder;

/**
 * User: mark
 * Date: 10/7/12
 * Time: 2:54 PM
 */
public class EmptyPersistenceFilter<P> implements PersistenceFilter<P>
{
    @Override
    public PredicateBuilder<P> getFilterPredicateBuilder(RequestContext requestContext)
    {
        return null;
    }

    @Override
    public boolean isVisible(P persistentObject, RequestContext requestContext)
    {
        return true;
    }

    @Override
    public boolean canCreate(P persistentObject, RequestContext requestContext)
    {
        return true;
    }

    @Override
    public boolean canUpdate(P persistentObject, RequestContext requestContext)
    {
        return true;
    }

    @Override
    public boolean canDelete(P persistentObject, RequestContext requestContext)
    {
        return true;
    }
}
