package com.dottydingo.hyperion.service.query;

import org.springframework.data.domain.Page;

/**
 */
public interface QueryHandler<T>
{
    Page<T> processQuery(String query,Integer start,Integer limit);
}

