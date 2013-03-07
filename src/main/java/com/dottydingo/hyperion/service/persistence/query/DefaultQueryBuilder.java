package com.dottydingo.hyperion.service.persistence.query;

import cz.jirutka.rsql.parser.model.Comparison;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 */
public class DefaultQueryBuilder<T> extends AbstractQueryBuilder<T>
{
    private String propertyName;

    public void setPropertyName(String propertyName)
    {
        this.propertyName = propertyName;
    }

    @Override
    public Predicate buildPredicate(From root, CriteriaBuilder cb, Comparison operator, String argument)
    {
        T parsed = argumentParser.parse(argument);
        return createPredicate(root,cb,propertyName,operator,argument);
    }
}
