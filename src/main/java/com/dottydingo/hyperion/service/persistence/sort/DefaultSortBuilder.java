package com.dottydingo.hyperion.service.persistence.sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;

/**
 * User: mark
 * Date: 2/24/13
 * Time: 9:18 AM
 */
public class DefaultSortBuilder implements SortBuilder
{
    private String propertyName;

    public void setPropertyName(String propertyName)
    {
        this.propertyName = propertyName;
    }

    @Override
    public List<Order> buildOrder(boolean desc, CriteriaBuilder cb, Root root)
    {
        if(desc)
            return Collections.singletonList(cb.desc(root.get(propertyName)));

        return Collections.singletonList(cb.asc(root.get(propertyName)));
    }
}
