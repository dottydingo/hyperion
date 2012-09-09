package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.service.model.BasePersistentObject;

import java.io.Serializable;
import java.util.List;

/**
 */
public interface PersistenceOperations<P extends BasePersistentObject,ID extends Serializable>
{
    P findById(ID id);

    List<P> findByIds(List<ID> ids);

    QueryResult<P> query(String query,Integer start, Integer limit);

    P createItem(P item);

    P updateItem(P item);

    void deleteItem(P item);
}
