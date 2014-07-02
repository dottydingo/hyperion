package com.dottydingo.hyperion.jpa.persistence.sort;

import com.dottydingo.hyperion.core.persistence.sort.PersistentOrderBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Order;
import java.util.List;

/**
 * Build the JPA Order instances at runtime
 */
public interface JpaPersistentOrderBuilder extends PersistentOrderBuilder
{
    List<Order> buildOrders( From root, CriteriaBuilder cb);
}
