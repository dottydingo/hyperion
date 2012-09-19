package com.dottydingo.hyperion.service.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * User: mark
 * Date: 9/15/12
 * Time: 5:29 PM
 */
public interface PredicateBuilder
{
    Predicate buildPredicate(String query, Class entityClass, Root root, CriteriaBuilder cb);
}
