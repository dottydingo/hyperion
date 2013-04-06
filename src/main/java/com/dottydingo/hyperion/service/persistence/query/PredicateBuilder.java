package com.dottydingo.hyperion.service.persistence.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 */
public interface PredicateBuilder<P>
{
   Predicate buildPredicate(Root<P> root, CriteriaQuery<?> query, CriteriaBuilder cb);
}
