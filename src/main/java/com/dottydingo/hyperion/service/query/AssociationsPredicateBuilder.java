package com.dottydingo.hyperion.service.query;

import cz.jirutka.rsql.parser.model.Comparison;


import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.EntityType;

/**
 * User: mark
 * Date: 9/16/12
 * Time: 8:34 PM
 */
public class AssociationsPredicateBuilder extends AbstractPredicateBuilder
{
    @Override
    public boolean accept(String propertyPath, Class<?> entityClass, ExpressionBuilder parent)
    {
        return splitPath(propertyPath).length > 1;
    }

    @Override
    public Predicate createPredicate(String propertyPath, Comparison operator, String argument, From root,
                                     String alias, ExpressionBuilder parent)
            throws ArgumentFormatException, UnknownSelectorException
    {
        String[] path = splitPath(propertyPath);
        String lastAlias = alias;
        String property = null;
        From lastRoot = root;

        // walk through associations
        for (int i = 0; i < path.length - 1; i++)
        {
            EntityType metadata = parent.getEntityType(lastRoot.getJavaType());
            property = parent.getMapper().translate(path[i], lastRoot.getJavaType());

            if (!isPropertyName(property, metadata))
            {
                throw new UnknownSelectorException(path[i]);
            }
            //Path objectPath = lastRoot.get(property);
            lastRoot = lastRoot.join(property);
            //lastRoot = findPropertyType(property, metadata);

            logger.trace("Nesting level {}: property '{}' of entity {}",
                    new Object[]{i, property, metadata.getJavaType().getSimpleName()});

            //lastAlias = parent.createAssociationAlias(lastAlias + property) + '.';
        }

        // the last property may by an ordinal property (not an association)
        property = parent.getMapper().translate(path[path.length - 1], lastRoot.getJavaType());

        return parent.delegateToBuilder(lastRoot,property, operator, argument);

    }

    protected String[] splitPath(String path)
    {
        return path.split("\\.");
    }

}
