package com.dottydingo.hyperion.service.persistence.sort;

import com.dottydingo.hyperion.service.persistence.PathIterator;

import javax.persistence.criteria.*;
import java.util.Collections;
import java.util.List;

/**
 */
public class DefaultSortBuilder implements SortBuilder
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
    public List<Order> buildOrder(boolean desc, CriteriaBuilder cb, Root root)
    {
        Path from = getFrom(root,PathIterator.getPath(propertyPath));
        Path path = from.get(propertyName);
        Order order = desc ? cb.desc(path) : cb.asc(path);
        return Collections.singletonList(order);
    }

    protected Path getFrom(Path from, PathIterator path)
    {
        if(path.hasNext())
            return getFrom(from.get(path.next()),path);
        return from;
    }
}
