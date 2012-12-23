package com.dottydingo.hyperion.service.sort;

import org.springframework.data.domain.Sort;

/**
 * User: mark
 * Date: 12/22/12
 * Time: 12:36 PM
 */
public interface SortBuilder
{
    Sort buildSort(String field,boolean descending);
}
