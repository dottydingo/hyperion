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
            String[] props = s1.split(":");
            String name = props[0].trim();

            // todo localize
            if(!validSorts.containsKey(name))
                throw new BadRequestException(
                        persistenceContext.getMessageSource().getErrorMessage(INVALID_SORT_FIELD,persistenceContext.getLocale(),name));

            if(name.equals("id"))
                containsIdSort = true;

            boolean desc = props.length == 2 && props[1].equalsIgnoreCase("desc");

            sort.addOrder(name,desc);
        }

        if(!containsIdSort && idSort != null)
            sort.addOrder("id");

        return sort;
    }
}
