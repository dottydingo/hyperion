package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.service.context.RequestContext;
import com.dottydingo.hyperion.service.model.PersistentObject;

import java.io.Serializable;
import java.util.List;

/**
 */
public interface PersistenceOperations<P extends PersistentObject,ID extends Serializable>
{
    P findById(ID id, RequestContext context);

    List<P> findByIds(List<ID> ids, RequestContext context);

    QueryResult<P> query(String query, Integer start, Integer limit, String sort, RequestContext context);

    P createItem(P item, RequestContext context);

    P updateItem(P item, RequestContext context);

    int deleteItem(P item, RequestContext context);
}
