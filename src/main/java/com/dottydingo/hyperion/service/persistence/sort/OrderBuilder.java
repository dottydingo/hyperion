package com.dottydingo.hyperion.service.persistence.sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * User: mark
 * Date: 2/24/13
 * Time: 6:40 AM
 */
public interface OrderBuilder<P>
{
    List<Order> buildOrders( Root<?> root, CriteriaBuilder cb);
}
