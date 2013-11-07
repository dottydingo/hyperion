package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.HistoryEntry;

import java.io.Serializable;
import java.util.List;

/**
 */
public interface PersistenceOperations<C extends ApiObject, ID extends Serializable>
{
    List<C> findByIds(List<ID> ids, PersistenceContext context);

    QueryResult<C> query(String query, Integer start, Integer limit, String sort, PersistenceContext context);

    C createOrUpdateItem(C item, PersistenceContext context);

    C updateItem(List<ID> ids, C item, PersistenceContext context);

    int deleteItem(List<ID> ids, PersistenceContext context);

    QueryResult<HistoryEntry> getHistory(ID id,Integer start, Integer limit,PersistenceContext context);
}
