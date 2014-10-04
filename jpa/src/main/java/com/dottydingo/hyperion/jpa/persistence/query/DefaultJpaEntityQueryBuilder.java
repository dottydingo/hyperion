package com.dottydingo.hyperion.jpa.persistence.query;

import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.hyperion.jpa.persistence.PathIterator;

import javax.persistence.criteria.*;
import java.util.List;

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
    public Predicate buildPredicate(From root, CriteriaQuery<?> query, CriteriaBuilder cb, ComparisonOperator operator,
                                    List<String> arguments, PersistenceContext persistenceContext)
    {
        Path from = getFrom(root,PathIterator.getPath(propertyPath));

        Object parsed = operator.supportsMultipleArguments()
                ? argumentParser.parse(arguments,from.get(propertyName).getJavaType(), persistenceContext)
                : argumentParser.parse(arguments.get(0),from.get(propertyName).getJavaType(), persistenceContext);

        return createPredicate(from, query, cb,propertyName,operator,parsed, persistenceContext);
    }

    protected Path getFrom(Path from, PathIterator path)
    {
        if(path.hasNext())
            return getFrom(from.get(path.next()),path);
        return from;
    }
}
