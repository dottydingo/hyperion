package com.dottydingo.hyperion.service.persistence.query;

import cz.jirutka.rsql.parser.model.Comparison;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * User: mark
 * Date: 2/24/13
 * Time: 7:03 AM
 */
public interface QueryBuilder
{
    Predicate buildPredicate(From root,CriteriaBuilder cb,Comparison operator, String argument);
}
