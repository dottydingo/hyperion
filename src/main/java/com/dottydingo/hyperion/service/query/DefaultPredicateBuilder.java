package com.dottydingo.hyperion.service.query;

import cz.jirutka.rsql.parser.model.Comparison;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.EntityType;

/**
 * User: mark
 * Date: 9/16/12
 * Time: 3:45 PM
 */
public class DefaultPredicateBuilder extends AbstractPredicateBuilder
{
    @Override
    public boolean accept(String property, Class<?> entityClass, ExpressionPredicateBuilder parent)
    {
        return true;
    }

    @Override
    public Predicate createPredicate(String property, Comparison operator, String argument, From root,
                                     ExpressionPredicateBuilder parent)
            throws ArgumentFormatException, UnknownSelectorException
    {

        EntityType metadata = parent.getEntityType(root.getJavaType());
        if (!isPropertyName(property, metadata))
        {
            throw new UnknownSelectorException(property);
        }

        Class<?> type = findPropertyType(property, metadata);
        Object castedArgument = parent.getArgumentParser().parse(argument, type);

        return createPredicate(root, parent.getCriteriaBuilder(), property, operator,
                castedArgument);
    }
}
