package com.dottydingo.hyperion.jpa.persistence.query;

import com.dottydingo.hyperion.jpa.persistence.PathIterator;
import cz.jirutka.rsql.parser.model.Comparison;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 */
public class DefaultJpaEntityQueryBuilder<T> extends AbstractEntityJpaQueryBuilder<T>
{
    private String propertyPath;
    private String propertyName;

    public void setPropertyPath(String propertyPath)
    {
        this.propertyPath = propertyPath;
    }

    public void setPropertyName(String propertyName)
    {
        this.propertyName = propertyName;
    }


    @Override
    public Predicate buildPredicate(From root, CriteriaBuilder cb, Comparison operator, String argument)
    {
        Path from = getFrom(root,PathIterator.getPath(propertyPath));

        Object parsed =  argumentParser.parse(argument,from.get(propertyName).getJavaType());
        return createPredicate(from,cb,propertyName,operator,parsed);
    }

    protected Path getFrom(Path from, PathIterator path)
    {
        if(path.hasNext())
            return getFrom(from.get(path.next()),path);
        return from;
    }
}
