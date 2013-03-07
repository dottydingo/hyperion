package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.service.context.RequestContext;
import com.dottydingo.hyperion.service.persistence.query.PredicateBuilder;

/**
 * User: mark
 * Date: 10/7/12
 * Time: 2:35 PM
 */
public interface PersistenceFilter<P>
{
    PredicateBuilder<P> getFilterPredicateBuilder(RequestContext requestContext);

    boolean isVisible(P persistentObject,RequestContext requestContext);

    boolean canCreate(P persistentObject, RequestContext requestContext);

    boolean canUpdate(P persistentObject, RequestContext requestContext);

    boolean canDelete(P persistentObject, RequestContext requestContext);
}
