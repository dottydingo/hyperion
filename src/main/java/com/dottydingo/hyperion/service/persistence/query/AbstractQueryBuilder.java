package com.dottydingo.hyperion.service.persistence.query;

import cz.jirutka.rsql.parser.model.Comparison;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * User: mark
 * Date: 2/24/13
 * Time: 7:20 AM
 */
public abstract class AbstractQueryBuilder<T> implements QueryBuilder
{
    private Logger logger = LoggerFactory.getLogger(AbstractQueryBuilder.class);
    protected static final Character LIKE_WILDCARD = '*';

    protected ArgumentParser<T> argumentParser;

    public void setArgumentParser(ArgumentParser<T> argumentParser)
    {
        this.argumentParser = argumentParser;
    }

    /**
     * Delegate creating of a Predicate to an appropriate method according to
     * operator.
     * <p/>
     *
     * @param propertyName property name
     * @param operator     comparison operator
     * @param argument     argument
     * @return Predicate
     */
    protected Predicate createPredicate(From root, CriteriaBuilder cb,  String propertyName, Comparison operator, Object argument)
    {
        logger.trace("Creating criterion: {} {} {}",
                new Object[]{propertyName, operator, argument});

        switch (operator)
        {
            case EQUAL:
            {
                if (containWildcard(argument))
                {
                    return createLike(root, cb, propertyName, argument);
                }
                else
                {
                    return createEqual(root, cb, propertyName, argument);
                }
            }
            case NOT_EQUAL:
            {
                if (containWildcard(argument))
                {
                    return createNotLike(root, cb, propertyName, argument);
                }
                else
                {
                    return createNotEqual(root, cb, propertyName, argument);
                }
            }
            case GREATER_THAN:
                return createGreaterThan(root, cb, propertyName, argument);
            case GREATER_EQUAL:
                return createGreaterEqual(root, cb, propertyName, argument);
            case LESS_THAN:
                return createLessThan(root, cb, propertyName, argument);
            case LESS_EQUAL:
                return createLessEqual(root, cb, propertyName, argument);
        }
        throw new IllegalArgumentException("Unknown operator: " + operator);
    }

    /**
     * Apply an "equal" constraint to the named property.
     *
     * @param propertyPath property name prefixed with an association alias
     * @param argument     value
     * @return Criterion
     */
    protected Predicate createEqual(From root, CriteriaBuilder cb,String propertyPath, Object argument)
    {
        return cb.equal(root.get(propertyPath), argument);
    }

    /**
     * Apply a case-insensitive "like" constraint to the named property. Value
     * should contains wildcards "*" (% in SQL) and "_".
     *
     * @param propertyPath property name prefixed with an association alias
     * @param argument     value
     * @return Criterion
     */
    protected Predicate createLike(From root, CriteriaBuilder cb,String propertyPath, Object argument)
    {
        String like = (String) argument;
        like = like.replace(LIKE_WILDCARD, '%');

        return cb.like(cb.lower(root.get(propertyPath)), like.toLowerCase());
    }

    /**
     * Apply a "not equal" constraint to the named property.
     *
     * @param propertyPath property name prefixed with an association alias
     * @param argument     value
     * @return Criterion
     */
    protected Predicate createNotEqual(From root, CriteriaBuilder cb,String propertyPath, Object argument)
    {
        return cb.notEqual(root.get(propertyPath), argument);
    }

    /**
     * Apply a negative case-insensitive "like" constraint to the named property.
     * Value should contains wildcards "*" (% in SQL) and "_".
     *
     * @param propertyPath property name prefixed with an association alias
     * @param argument     Value with wildcards.
     * @return Criterion
     */
    protected Predicate createNotLike(From root, CriteriaBuilder cb,String propertyPath, Object argument)
    {
        return cb.notLike(root.get(propertyPath), (String) argument);
    }

    /**
     * Apply a "greater than" constraint to the named property.
     *
     * @param propertyPath property name prefixed with an association alias
     * @param argument     value
     * @return Criterion
     */
    protected Predicate createGreaterThan(From root, CriteriaBuilder cb,String propertyPath, Object argument)
    {
        return cb.gt(root.get(propertyPath),(Number) argument);
    }

    /**
     * Apply a "greater than or equal" constraint to the named property.
     *
     * @param propertyPath property name prefixed with an association alias
     * @param argument     value
     * @return Criterion
     */
    protected Predicate createGreaterEqual(From root, CriteriaBuilder cb,String propertyPath, Object argument)
    {
        return cb.ge(root.get(propertyPath), (Number)argument);
    }

    /**
     * Apply a "less than" constraint to the named property.
     *
     * @param propertyPath property name prefixed with an association alias
     * @param argument     value
     * @return Criterion
     */
    protected Predicate createLessThan(From root, CriteriaBuilder cb,String propertyPath, Object argument)
    {
        return cb.lt(root.get(propertyPath), (Number)argument);
    }

    /**
     * Apply a "less than or equal" constraint to the named property.
     *
     * @param propertyPath property name prefixed with an association alias
     * @param argument     value
     * @return Criterion
     */
    protected Predicate createLessEqual(From root, CriteriaBuilder cb,String propertyPath, Object argument)
    {
        return cb.le(root.get(propertyPath), (Number)argument);
    }

    /**
     * Check if given argument contains wildcard.
     *
     * @param argument
     * @return Return <tt>true</tt> if argument contains wildcard
     *         {@link #LIKE_WILDCARD}.
     */
    protected boolean containWildcard(Object argument)
    {
        if (!(argument instanceof String))
        {
            return false;
        }

        String casted = (String) argument;
        return casted.contains(LIKE_WILDCARD.toString());

    }
}
