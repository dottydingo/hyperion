package com.dottydingo.hyperion.core.persistence;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.HistoryEntry;
import com.dottydingo.hyperion.core.endpoint.EndpointSort;
import cz.jirutka.rsql.parser.ast.Node;

import java.io.Serializable;
import java.util.List;

/**
 */
public interface PersistenceOperations<C extends ApiObject, ID extends Serializable>
{
    List<C> findByIds(List<ID> ids, PersistenceContext context);

    QueryResult<C> query(Node query, Integer start, Integer limit, EndpointSort sort, PersistenceContext context);

    List<C> createOrUpdateItems(List<C> clientItems, PersistenceContext context);

    List<C> updateItems(List<C> clientItems, PersistenceContext context);

    C updateItem(ID id, C item, PersistenceContext context);

    int deleteItem(List<ID> ids, PersistenceContext context);

    QueryResult<HistoryEntry> getHistory(ID id,Integer start, Integer limit,PersistenceContext context);
}
