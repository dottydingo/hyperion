package com.dottydingo.hyperion.jpa.persistence.query;

import com.dottydingo.hyperion.api.exception.BadRequestException;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.hyperion.core.persistence.query.ArgumentParser;
import com.dottydingo.hyperion.core.persistence.query.DefaultArgumentParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.Collection;

/**
 */
public abstract class AbstractEntityJpaQueryBuilder<T> implements JpaEntityQueryBuilder
{
    public static final String INCOMPATIBLE_QUERY_OPERATION = "ERROR_INCOMPATIBLE_QUERY_OPERATION";
    private Logger logger = LoggerFactory.getLogger(AbstractEntityJpaQueryBuilder.class);
    protected static final Character LIKE_WILDCARD = '*';

    protected ArgumentParser argumentParser = DefaultArgumentParser.getInstance();

    public void setArgumentParser(ArgumentParser argumentParser)
    {
        this.argumentParser = argumentParser;
    }

    /**
     * Delegate creating of a Predicate to an appropriate method according to
     * operator.
     *
     * @param root         root
     * @param query        query
     * @param propertyName property name
     * @param operator     comparison operator
     * @param argument     argument
     * @param context      context
     * @return Predicate
     */
    protected Predicate createPredicate(Path root, CriteriaQuery<?> query, CriteriaBuilder cb, String propertyName,
                                        ComparisonOperator operator, Object argument, PersistenceContext context)
    {
        logger.debug("Creating criterion: {} {} {}", propertyName, operator, argument);

        switch (operator)
        {
            case EQUAL:
            {
                if (containWildcard(argument))
                {
                    return createLike(root, query, cb, propertyName, argument, context);
                }
                else
                {
                    return createEqual(root, query, cb, propertyName, argument, context);
                }
            }
            case NOT_EQUAL:
            {
                if (containWildcard(argument))
                {
                    return createNotLike(root, query, cb, propertyName, argument, context);
                }
                else
                {
                    return createNotEqual(root, query, cb, propertyName, argument, context);
                }
            }
            case GREATER_THAN:
                return createGreaterThan(root, query, cb, propertyName, argument, context);
            case GREATER_EQUAL:
                return createGreaterEqual(root, query, cb, propertyName, argument, context);
            case LESS_THAN:
                return createLessThan(root, query, cb, propertyName, argument, context);
            case LESS_EQUAL:
                return createLessEqual(root, query, cb, propertyName, argument, context);
            case IN:
                return createIn(root, query, cb, propertyName, argument, context);
            case NOT_IN:
                return createNotIn(root, query, cb, propertyName, argument, context);
        }
        throw new IllegalArgumentException("Unknown operator: " + operator);
    }

    /**
     * Apply an "equal" constraint to the named property.
     *
     *
     * @param query
     * @param propertyPath property name prefixed with an association alias
     * @param argument     value
     * @param context
     * @return Predicate
     */
    protected Predicate createEqual(Path root, CriteriaQuery<?> query, CriteriaBuilder cb, String propertyPath,
                                    Object argument, PersistenceContext context)
    {
        return cb.equal(root.get(propertyPath), argument);
    }

    /**
     * Apply a case-insensitive "like" constraint to the named property. Value
     * should contains wildcards "*" (% in SQL) and "_".
     *
     *
     * @param query
     * @param propertyPath property name prefixed with an association alias
     * @param argument     value
     * @param context
     * @return Predicate
     */
    protected Predicate createLike(Path root, CriteriaQuery<?> query, CriteriaBuilder cb, String propertyPath, Object argument,
                                   PersistenceContext context)
    {
        String like = (String) argument;
        like = like.replace(LIKE_WILDCARD, '%');

        return cb.like(cb.lower(root.get(propertyPath)), like.toLowerCase());
    }

    /**
     * Apply a "not equal" constraint to the named property.
     *
     *
     * @param query
     * @param propertyPath property name prefixed with an association alias
     * @param argument     value
     * @param context
     * @return Predicate
     */
    protected Predicate createNotEqual(Path root, CriteriaQuery<?> query, CriteriaBuilder cb, String propertyPath, Object argument,
                                       PersistenceContext context)
    {
        return cb.notEqual(root.get(propertyPath), argument);
    }

    /**
     * Apply a negative case-insensitive "like" constraint to the named property.
     * Value should contains wildcards "*" (% in SQL) and "_".
     *
     *
     * @param query
     * @param propertyPath property name prefixed with an association alias
     * @param argument     Value with wildcards.
     * @param context
     * @return Predicate
     */
    protected Predicate createNotLike(Path root, CriteriaQuery<?> query, CriteriaBuilder cb, String propertyPath, Object argument,
                                      PersistenceContext context)
    {
        return cb.notLike(root.get(propertyPath), (String) argument);
    }

    /**
     * Apply a "greater than" constraint to the named property.
     *
     *
     * @param query
     * @param propertyPath property name prefixed with an association alias
     * @param argument     value
     * @param context
     * @return Predicate
     */
    protected Predicate createGreaterThan(Path root, CriteriaQuery<?> query, CriteriaBuilder cb, String propertyPath, Object argument,
                                          PersistenceContext context)
    {
        if(!(argument instanceof Comparable))
            throw new BadRequestException(context.getMessageSource().getErrorMessage(
                    INCOMPATIBLE_QUERY_OPERATION, context.getLocale(), "gt"));

        return cb.greaterThan(root.get(propertyPath), (Comparable) argument);
    }

    /**
     * Apply a "greater than or equal" constraint to the named property.
     *
     *
     * @param query
     * @param propertyPath property name prefixed with an association alias
     * @param argument     value
     * @param context
     * @return Predicate
     */
    protected Predicate createGreaterEqual(Path root, CriteriaQuery<?> query, CriteriaBuilder cb, String propertyPath, Object argument,
                                           PersistenceContext context)
    {
        if(!(argument instanceof Comparable))
            throw new BadRequestException(context.getMessageSource().getErrorMessage(
                    INCOMPATIBLE_QUERY_OPERATION, context.getLocale(), "ge"));

        return cb.greaterThanOrEqualTo(root.get(propertyPath), (Comparable) argument);
    }

    /**
     * Apply a "less than" constraint to the named property.
     *
     *
     * @param query
     * @param propertyPath property name prefixed with an association alias
     * @param argument     value
     * @param context
     * @return Predicate
     */
    protected Predicate createLessThan(Path root, CriteriaQuery<?> query, CriteriaBuilder cb, String propertyPath, Object argument,
                                       PersistenceContext context)
    {
        if(!(argument instanceof Comparable))
            throw new BadRequestException(context.getMessageSource().getErrorMessage(
                    INCOMPATIBLE_QUERY_OPERATION, context.getLocale(), "lt"));

        return cb.lessThan(root.get(propertyPath), (Comparable) argument);
    }

    /**
     * Apply a "less than or equal" constraint to the named property.
     *
     *
     * @param query
     * @param propertyPath property name prefixed with an association alias
     * @param argument     value
     * @param context
     * @return Predicate
     */
    protected Predicate createLessEqual(Path root, CriteriaQuery<?> query, CriteriaBuilder cb, String propertyPath, Object argument,
                                        PersistenceContext context)
    {
        if(!(argument instanceof Comparable))
            throw new BadRequestException(context.getMessageSource().getErrorMessage(
                    INCOMPATIBLE_QUERY_OPERATION,context.getLocale(),"le"));

        return cb.lessThanOrEqualTo(root.get(propertyPath), (Comparable) argument);
    }

    protected Predicate createIn(Path root, CriteriaQuery<?> query, CriteriaBuilder cb, String propertyPath, Object argument,
                                 PersistenceContext context)
    {
        if(!(argument instanceof Collection))
            throw new BadRequestException(context.getMessageSource().getErrorMessage(
                    INCOMPATIBLE_QUERY_OPERATION, context.getLocale(), "in"));

        Path expression = root.get(propertyPath);
        return expression.in((Collection)argument);
    }

    protected Predicate createNotIn(Path root, CriteriaQuery<?> query, CriteriaBuilder cb, String propertyPath, Object argument,
                                    PersistenceContext context)
    {
        if(!(argument instanceof Collection))
            throw new BadRequestException(context.getMessageSource().getErrorMessage(
                    INCOMPATIBLE_QUERY_OPERATION, context.getLocale(), "not in"));

        Path expression = root.get(propertyPath);
        return cb.not(expression.in((Collection)argument));
    }

    /**
     * Check if given value contains wildcard.
     *
     * @param value the value
     * @return Return <tt>true</tt> if argument contains {@link #LIKE_WILDCARD}.
     */
    protected boolean containWildcard(Object value)
    {
        if (!(value instanceof String))
        {
            return false;
        }

        String casted = (String) value;
        return casted.contains(LIKE_WILDCARD.toString());

    }
}
