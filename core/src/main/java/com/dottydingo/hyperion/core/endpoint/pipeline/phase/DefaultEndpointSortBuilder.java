package com.dottydingo.hyperion.core.endpoint.pipeline.phase;


import com.dottydingo.hyperion.api.exception.BadRequestException;
import com.dottydingo.hyperion.core.registry.EntitySortBuilder;
import com.dottydingo.hyperion.core.endpoint.EndpointSort;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;

import java.util.Map;

/**
 */
public class DefaultEndpointSortBuilder implements EndpointSortBuilder
{

    private static final String INVALID_SORT_FIELD = "ERROR_INVALID_SORT_FIELD";

    @Override
    public EndpointSort buildSort(String sortString, PersistenceContext persistenceContext)
    {

        if(sortString == null || sortString.trim().length() == 0)
            return null;

        Map<String,EntitySortBuilder> validSorts = persistenceContext.getApiVersionPlugin().getSortBuilders();

        EntitySortBuilder idSort = validSorts.get("id");
        boolean containsIdSort = false;
        EndpointSort sort = new EndpointSort();
        String[] split = sortString.split(",");
        for (String s1 : split)
        {
            EndpointSort.EndpointOrder order = null;
            String trimmed = s1.trim();
            if(trimmed.startsWith("-"))
            {
                order = new EndpointSort.EndpointOrder(trimmed.length() > 1 ? trimmed.substring(1) : "",true);
            }
            else
            {
                String[] props = trimmed.split(":");
                String name = props[0].trim();
                boolean desc = props.length == 2 && props[1].equalsIgnoreCase("desc");
                order = new EndpointSort.EndpointOrder(name,desc);
            }

            if(!validSorts.containsKey(order.getField()))
                throw new BadRequestException(
                        persistenceContext.getMessageSource().getErrorMessage(INVALID_SORT_FIELD,
                                persistenceContext.getLocale(),order.getField()));

            if(order.getField().equals("id"))
                containsIdSort = true;

            sort.addOrder(order);
        }

        if(!containsIdSort && idSort != null)
            sort.addOrder("id");

        return sort;
    }


}
