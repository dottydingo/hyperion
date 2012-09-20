package com.dottydingo.hyperion.service.query;

import cz.jirutka.rsql.parser.model.Comparison;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;

/**
 * User: mark
 * Date: 9/16/12
 * Time: 3:12 PM
 */
public interface ExpressionPredicateBuilder
{
    ArgumentParser getArgumentParser();

    Mapper getMapper();

    EntityType getEntityType(Class<?> entityClass);

    CriteriaBuilder getCriteriaBuilder();

    Predicate buildPredicate(From entityRoot, String property, Comparison operator, String argument)
            throws ArgumentFormatException, UnknownSelectorException, IllegalArgumentException;
}
