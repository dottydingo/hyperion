package com.dottydingo.hyperion.jpa.persistence.query;

import com.dottydingo.hyperion.api.exception.BadRequestException;
import com.dottydingo.hyperion.core.persistence.query.ArgumentParser;
import com.dottydingo.hyperion.core.persistence.query.DefaultArgumentParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.Collection;

/**
 */
public abstract class AbstractEntityJpaQueryBuilder<T> implements JpaEntityQueryBuilder
{
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
     * <p/>
     *
     * @param propertyName property name
     * @param operator     comparison operator
     * @param argument     argument
     * @return Predicate
     */
    protected Predicate createPredicate(Path root, CriteriaBuilder cb,  String propertyName, ComparisonOperator operator, Object argument)
    {
        logger.debug("Creating criterion: {} {} {}", propertyName, operator, argument);

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
            case IN:
                return createIn(root, cb, propertyName, argument);
            case NOT_IN:
                return createNotIn(root, cb, propertyName, argument);
        }
        throw new IllegalArgumentException("Unknown operator: " + operator);
    }

    /**
     * Apply an "equal" constraint to the named property.
     *
     * @param propertyPath property name prefixed with an association alias
     * @param argument     value
     * @return Predicate
     */
    protected Predicate createEqual(Path root, CriteriaBuilder cb,String propertyPath, Object argument)
    {
        return cb.equal(root.get(propertyPath), argument);
    }

    /**
     * Apply a case-insensitive "like" constraint to the named property. Value
     * should contains wildcards "*" (% in SQL) and "_".
     *
     * @param propertyPath property name prefixed with an association alias
     * @param argument     value
     * @return Predicate
     */
    protected Predicate createLike(Path root, CriteriaBuilder cb,String propertyPath, Object argument)
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
     * @return Predicate
     */
    protected Predicate createNotEqual(Path root, CriteriaBuilder cb,String propertyPath, Object argument)
    {
        return cb.notEqual(root.get(propertyPath), argument);
    }

    /**
     * Apply a negative case-insensitive "like" constraint to the named property.
     * Value should contains wildcards "*" (% in SQL) and "_".
     *
     * @param propertyPath property name prefixed with an association alias
     * @param argument     Value with wildcards.
     * @return Predicate
     */
    protected Predicate createNotLike(Path root, CriteriaBuilder cb,String propertyPath, Object argument)
    {
        return cb.notLike(root.get(propertyPath), (String) argument);
    }

    /**
     * Apply a "greater than" constraint to the named property.
     *
     * @param propertyPath property name prefixed with an association alias
     * @param argument     value
     * @return Predicate
     */
    protected Predicate createGreaterThan(Path root, CriteriaBuilder cb,String propertyPath, Object argument)
    {
        if(!(argument instanceof Comparable))
            throw new BadRequestException(String.format("Incompatible query operation: %s.","gt"));

        return cb.greaterThan(root.get(propertyPath), (Comparable) argument);
    }

    /**
     * Apply a "greater than or equal" constraint to the named property.
     *
     * @param propertyPath property name prefixed with an association alias
     * @param argument     value
     * @return Predicate
     */
    protected Predicate createGreaterEqual(Path root, CriteriaBuilder cb,String propertyPath, Object argument)
    {
        if(!(argument instanceof Comparable))
            throw new BadRequestException(String.format("Incompatible query operation: %s.","ge"));

        return cb.greaterThanOrEqualTo(root.get(propertyPath), (Comparable) argument);
    }

    /**
     * Apply a "less than" constraint to the named property.
     *
     * @param propertyPath property name prefixed with an association alias
     * @param argument     value
     * @return Predicate
     */
    protected Predicate createLessThan(Path root, CriteriaBuilder cb,String propertyPath, Object argument)
    {
        if(!(argument instanceof Comparable))
            throw new BadRequestException(String.format("Incompatible query operation: %s.","lt"));

        return cb.lessThan(root.get(propertyPath), (Comparable) argument);
    }

    /**
     * Apply a "less than or equal" constraint to the named property.
     *
     * @param propertyPath property name prefixed with an association alias
     * @param argument     value
     * @return Predicate
     */
    protected Predicate createLessEqual(Path root, CriteriaBuilder cb,String propertyPath, Object argument)
    {
        if(!(argument instanceof Comparable))
            throw new BadRequestException(String.format("Incompatible query operation: %s.","le"));

        return cb.lessThanOrEqualTo(root.get(propertyPath), (Comparable) argument);
    }

    protected Predicate createIn(Path root, CriteriaBuilder cb,String propertyPath, Object argument)
    {
        if(!(argument instanceof Collection))
            throw new BadRequestException(String.format("Incompatible query operation: %s.","in"));

        Path expression = root.get(propertyPath);
        return expression.in((Collection)argument);
    }

    protected Predicate createNotIn(Path root, CriteriaBuilder cb,String propertyPath, Object argument)
    {
        if(!(argument instanceof Collection))
            throw new BadRequestException(String.format("Incompatible query operation: %s.", "in"));

        Path expression = root.get(propertyPath);
        return cb.not(expression.in((Collection)argument));
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
