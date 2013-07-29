package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.service.context.PersistenceContext;
import com.dottydingo.hyperion.service.model.BasePersistentHistoryEntry;

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

    <H extends BasePersistentHistoryEntry<ID>> List<H> getHistory(ID id,Integer start, Integer limit,PersistenceContext context);
}
