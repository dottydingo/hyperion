package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.service.context.RequestContext;
import org.springframework.data.jpa.domain.Specification;

/**
 * User: mark
 * Date: 10/7/12
 * Time: 2:35 PM
 */
public interface PersistenceFilter<P>
{
    Specification<P> getFilterSpecification(RequestContext requestContext);

    boolean isVisible(P persistentObject,RequestContext requestContext);

    boolean canCreate(P persistentObject, RequestContext requestContext);

    boolean canUpdate(P persistentObject, RequestContext requestContext);

    boolean canDelete(P persistentObject, RequestContext requestContext);
}
