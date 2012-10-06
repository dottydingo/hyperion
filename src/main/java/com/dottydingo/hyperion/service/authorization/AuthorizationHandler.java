package com.dottydingo.hyperion.service.authorization;

import com.dottydingo.hyperion.service.context.AuthorizationContext;
import org.springframework.data.jpa.domain.Specification;

/**
 */
public interface AuthorizationHandler<T>
{
    Specification<T> getSpecification(AuthorizationContext authorizationContext);
}
