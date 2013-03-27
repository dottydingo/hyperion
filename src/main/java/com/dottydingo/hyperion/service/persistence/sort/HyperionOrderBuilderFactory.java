package com.dottydingo.hyperion.service.persistence.sort;

import com.dottydingo.hyperion.exception.BadRequestException;
import com.dottydingo.hyperion.service.configuration.EntityPlugin;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**

 */
public class HyperionOrderBuilderFactory<P> implements OrderBuilderFactory<P>
{

    @Override
    public OrderBuilder<P> createOrderBuilder(String sortString, EntityPlugin entityPlugin)
    {
        return new DefaultOrderBuilder(sortString,entityPlugin.getSortBuilders());
    }

    private class DefaultOrderBuilder implements OrderBuilder<P>
    {
        private String sortString;
        private  Map<String, SortBuilder> sortBuilders;


        private DefaultOrderBuilder(String sortString, Map<String, SortBuilder> sortBuilders)
        {
            this.sortString = sortString;
            this.sortBuilders = sortBuilders;
        }

        @Override
        public List<Order> buildOrders(Root<?> root, CriteriaBuilder cb)
        {
            SortBuilder idSortBuilder = sortBuilders.get("id");

            if (sortString == null || sortString.length() == 0)
            {
                if (idSortBuilder != null)
                {
                    return idSortBuilder.buildOrder(false, cb, root);
                }

                return Collections.emptyList();
            }
            List<Order> orderList = new ArrayList<Order>();

            boolean hasId = false;
            String[] split = sortString.split(",");
            for (String s1 : split)
            {
                String[] props = s1.split(":");
                String name = props[0].trim();
                if (name.equals("id"))
                {
                    hasId = true;
                }
                boolean desc = props.length == 2 && props[1].equalsIgnoreCase("desc");

                SortBuilder sortBuilder = sortBuilders.get(name);
                if (sortBuilder == null)
                {
                    throw new BadRequestException(String.format("%s is not a valid sort field.", name));
                }

                orderList.addAll(sortBuilder.buildOrder(desc, cb, root));
            }

            if (!hasId && idSortBuilder != null)
            {
                orderList.addAll(idSortBuilder.buildOrder(false, cb, root));
            }

            return orderList;
        }
    }
}
