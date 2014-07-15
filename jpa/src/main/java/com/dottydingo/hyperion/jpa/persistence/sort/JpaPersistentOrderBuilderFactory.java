package com.dottydingo.hyperion.jpa.persistence.sort;

import com.dottydingo.hyperion.api.exception.InternalException;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.hyperion.core.endpoint.EndpointSort;
import com.dottydingo.hyperion.core.persistence.sort.PersistentOrderBuilderFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Order;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**

 */
public class JpaPersistentOrderBuilderFactory implements PersistentOrderBuilderFactory<JpaPersistentOrderBuilder>
{

    @Override
    public JpaPersistentOrderBuilder createOrderBuilder(EndpointSort endpointSort, PersistenceContext persistenceContext)
    {
        return new DefaultOrderBuilder(endpointSort,persistenceContext.getApiVersionPlugin().getSortBuilders());
    }

    private class DefaultOrderBuilder implements JpaPersistentOrderBuilder
    {
        private EndpointSort endpointSort;
        private  Map<String, JpaEntitySortBuilder> sortBuilders;


        private DefaultOrderBuilder(EndpointSort endpointSort, Map<String, JpaEntitySortBuilder> sortBuilders)
        {
            this.endpointSort = endpointSort;
            this.sortBuilders = sortBuilders;
        }

        @Override
        public List<Order> buildOrders(From root, CriteriaBuilder cb)
        {
            JpaEntitySortBuilder idSortBuilder = sortBuilders.get("id");

            if (endpointSort == null || endpointSort.getOrders().size() == 0)
            {
                if (idSortBuilder != null)
                {
                    return idSortBuilder.buildOrder(false, cb, root);
                }

                return Collections.emptyList();
            }
            List<Order> orderList = new ArrayList<Order>();

            boolean hasId = false;
            for (EndpointSort.EndpointOrder endpointOrder : endpointSort.getOrders())
            {
                JpaEntitySortBuilder sortBuilder = sortBuilders.get(endpointOrder.getField());

                // this should never happen since it gets validated elsewhere
                if(sortBuilder == null)
                    throw new InternalException(String.format("%s is not a valid sort field.", endpointOrder.getField()));

                if(endpointOrder.getField().equals("id"))
                    hasId = true;

                orderList.addAll(sortBuilder.buildOrder(endpointOrder.isDescending(), cb, root));
            }
            if (!hasId && idSortBuilder != null)
            {
                orderList.addAll(idSortBuilder.buildOrder(false, cb, root));
            }

            return orderList;
        }
    }
}
