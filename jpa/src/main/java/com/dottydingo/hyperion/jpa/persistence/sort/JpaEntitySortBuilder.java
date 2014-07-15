package com.dottydingo.hyperion.jpa.persistence.sort;


import com.dottydingo.hyperion.core.registry.EntitySortBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Order;
import java.util.List;

/**
 * User: mark
 * Date: 12/22/12
 * Time: 12:36 PM
 */
public interface JpaEntitySortBuilder extends EntitySortBuilder
{
    List<Order> buildOrder(boolean desc, CriteriaBuilder cb, From root);
}
