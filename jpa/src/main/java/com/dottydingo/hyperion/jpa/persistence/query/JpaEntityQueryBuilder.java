package com.dottydingo.hyperion.jpa.persistence.query;

import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.hyperion.core.registry.EntityQueryBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * A JPA query builder
 */
public interface JpaEntityQueryBuilder extends EntityQueryBuilder
{
    Predicate buildPredicate(From root, CriteriaQuery<?> query, CriteriaBuilder cb, ComparisonOperator operator, List<String> arguments,
                             PersistenceContext persistenceContext);
}
