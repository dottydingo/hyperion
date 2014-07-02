package com.dottydingo.hyperion.jpa.persistence.query;

import com.dottydingo.hyperion.core.persistence.query.PersistentQueryBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 */
public interface JpaPersistentQueryBuilder<P> extends PersistentQueryBuilder
{
   Predicate buildPredicate(Root<P> root, CriteriaQuery<?> query, CriteriaBuilder cb);
}
