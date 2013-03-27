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
    private Class<T> propertyType;

    public void setPropertyName(String propertyName)
    {
        this.propertyName = propertyName;
    }

    public void setPropertyType(Class<T> propertyType)
    {
        this.propertyType = propertyType;
    }

    @Override
    public Predicate buildPredicate(From root, CriteriaBuilder cb, Comparison operator, String argument)
    {
        T parsed = argumentParser.parse(argument,propertyType);
        return createPredicate(root,cb,propertyName,operator,parsed);
    }
}
