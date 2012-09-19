package com.dottydingo.hyperion.service.query;

import cz.jirutka.rsql.parser.model.Comparison;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;

/**
 */
public abstract class AbstractPredicateBuilder
{
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected static final Character LIKE_WILDCARD = '*';

    /**
     * This method is called by Criteria Builder to determine if this builder
     * can handle given comparison (constraint).
     *
     * @param property    property name or path
     * @param entityClass Class of entity that holds given property.
     * @param parent      Reference to the parent <tt>CriteriaBuilder</tt>.
     * @return <tt>true</tt> if this builder can handle given property of entity
     *         class, otherwise <tt>false</tt>
     */
    public abstract boolean accept(String property, Class<?> entityClass, ExpressionBuilder parent);

    /**
     * Create <tt>Criterion</tt> for given comparison (constraint).
     *
     *
     * @param property    property name or path
     * @param operator    comparison operator
     * @param argument    argument
     * @param root
     * @param alias       Association alias (incl. dot) which must be used to prefix
*                    property name!
     * @param parent      Reference to the parent <tt>CriteriaBuilder</tt>.   @return Criterion
     * @throws ArgumentFormatException
     *          If given argument is not in suitable
     *          format required by entity's property, i.e. cannot be cast to
     *          property's type.
     * @throws UnknownSelectorException
     *          If such property does not exist.
     */
    public abstract Predicate createPredicate(String property, Comparison operator, String argument,
                                              From root, String alias, ExpressionBuilder parent)
            throws ArgumentFormatException, UnknownSelectorException;

    /**
     * Delegate creating of a Criterion to an appropriate method according to
     * operator.
     * <p/>
     * Property name MUST be prefixed with an association alias!
     *
     * @param propertyPath property name prefixed with an association alias
     * @param operator     comparison operator
     * @param argument     argument
     * @return Criterion
     */
    protected Predicate createPredicate(From root, CriteriaBuilder cb,  String propertyPath, Comparison operator, Object argument)
    {
        logger.trace("Creating criterion: {} {} {}",
                new Object[]{propertyPath, operator, argument});

        switch (operator)
        {
            case EQUAL:
            {
                if (containWildcard(argument))
                {
                    return createLike(root, cb, propertyPath, argument);
                }
                else
                {
                    return createEqual(root, cb, propertyPath, argument);
                }
            }
            case NOT_EQUAL:
            {
                if (containWildcard(argument))
                {
                    return createNotLike(root, cb, propertyPath, argument);
                }
                else
                {
                    return createNotEqual(root, cb, propertyPath, argument);
                }
            }
            case GREATER_THAN:
                return createGreaterThan(root, cb, propertyPath, argument);
            case GREATER_EQUAL:
                return createGreaterEqual(root, cb, propertyPath, argument);
            case LESS_THAN:
                return createLessThan(root, cb, propertyPath, argument);
            case LESS_EQUAL:
                return createLessEqual(root, cb, propertyPath, argument);
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
        if (casted.contains(LIKE_WILDCARD.toString()))
        {
            return true;
        }

        return false;
    }

    /**
     * Check if entity of specified class metadata contains given property.
     *
     * @param property      property name
     * @param classMetadata entity metadata
     * @return <tt>true</tt> if specified class metadata contains given property,
     *         otherwise <tt>false</tt>.
     */
    protected boolean isPropertyName(String property, EntityType classMetadata)
    {
        Attribute attributes = classMetadata.getAttribute(property);

        return attributes != null;
    }

    /**
     * Find the java type class of given named property in entity's metadata.
     *
     * @param property      property name
     * @param classMetadata entity metadata
     * @return The java type class of given property.
     */
    protected Class<?> findPropertyType(String property, EntityType classMetadata)
    {
        return classMetadata.getAttribute(property).getJavaType();
    }

}
