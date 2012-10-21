package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.service.context.RequestContext;

import java.io.Serializable;
import java.util.List;

/**
 */
public interface PersistenceOperations<C extends ApiObject, ID extends Serializable>
{
    List<C> findByIds(List<ID> ids, RequestContext context);

    QueryResult<C> query(String query, Integer start, Integer limit, String sort, RequestContext context);

    C createItem(C item, RequestContext context);

    C updateItem(C item, RequestContext context);

    int deleteItem(List<ID> ids, RequestContext context);
}
