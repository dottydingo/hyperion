package com.dottydingo.hyperion.service.persistence.sort;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * User: mark
 * Date: 12/22/12
 * Time: 12:36 PM
 */
public interface SortBuilder
{
    List<Order> buildOrder(boolean desc, CriteriaBuilder cb, Root root);
}
