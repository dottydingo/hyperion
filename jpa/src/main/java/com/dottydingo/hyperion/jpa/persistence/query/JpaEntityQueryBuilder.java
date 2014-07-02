package com.dottydingo.hyperion.jpa.persistence.query;

import com.dottydingo.hyperion.core.registry.EntityQueryBuilder;
import cz.jirutka.rsql.parser.model.Comparison;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * A JPA query builder
 */
public interface JpaEntityQueryBuilder extends EntityQueryBuilder
{
    Predicate buildPredicate(From root,CriteriaBuilder cb,Comparison operator, String argument);
}
