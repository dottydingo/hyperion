package com.dottydingo.hyperion.core.endpoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 */
public class EndpointSort
{
    private List<EndpointOrder> orderList = new ArrayList<EndpointOrder>();

    public EndpointSort()
    {

    }

    public EndpointSort(EndpointOrder... orders)
    {
        if(orders != null && orders.length>0)
            orderList = Arrays.asList(orders);
    }

    public EndpointSort addOrder(EndpointOrder order)
    {
        orderList.add(order);
        return this;
    }

    public EndpointSort addOrder(String property, boolean descending)
    {
        orderList.add(new EndpointOrder(property,descending));
        return this;
    }

    public EndpointSort addOrder(String property)
    {
        orderList.add(new EndpointOrder(property));
        return this;
    }

    public List<EndpointOrder> getOrders()
    {
        return orderList;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof EndpointSort))
        {
            return false;
        }

        EndpointSort that = (EndpointSort) o;

        if (orderList != null ? !orderList.equals(that.orderList) : that.orderList != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return orderList != null ? orderList.hashCode() : 0;
    }

    /**
     */
    public static class EndpointOrder
    {
        private String field;
        private boolean descending = false;

        public EndpointOrder(String field)
        {
            this(field,false);
        }

        public EndpointOrder(String field, boolean descending)
        {
            this.field = field;
            this.descending = descending;
        }

        public String getField()
        {
            return field;
        }

        public boolean isDescending()
        {
            return descending;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (!(o instanceof EndpointOrder))
            {
                return false;
            }

            EndpointOrder order = (EndpointOrder) o;

            if (descending != order.descending)
            {
                return false;
            }
            if (field != null ? !field.equals(order.field) : order.field != null)
            {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode()
        {
            int result = field != null ? field.hashCode() : 0;
            result = 31 * result + (descending ? 1 : 0);
            return result;
        }
    }


}
