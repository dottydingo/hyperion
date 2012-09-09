package com.dottydingo.hyperion.service.authorization;

import org.springframework.data.jpa.domain.Specification;

/**
 */
public interface AuthorizationHandler<T>
{
    Specification<T> getSpecification(AuthorizationContext authorizationContext);
}
