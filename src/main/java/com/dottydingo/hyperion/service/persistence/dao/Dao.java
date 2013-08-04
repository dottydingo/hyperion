package com.dottydingo.hyperion.service.persistence.dao;

import com.dottydingo.hyperion.service.model.BasePersistentHistoryEntry;
import com.dottydingo.hyperion.service.persistence.query.PredicateBuilder;
import com.dottydingo.hyperion.service.persistence.sort.OrderBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * User: mark
 * Date: 1/27/13
 * Time: 9:30 AM
 */
public interface Dao<P,ID extends Serializable>
{
    List<P> findAll(Class<P> entityClass, List<ID> ids);

    PersistentQueryResult<P> query(Class<P> entityClass, Integer start, Integer Limit, OrderBuilder orderBuilder,
                  List<PredicateBuilder<P>> predicateBuilders);

    P find(Class<P> entityClass, ID id);

    P create(P entity);

    P update(P entity);

    void delete(P entity);

    <H extends BasePersistentHistoryEntry<ID>> PersistentQueryResult<H> getHistory(Class<H> historyType, String entityType, ID entityId, Integer start,
                                                                  Integer limit);

    <H extends BasePersistentHistoryEntry<ID>> void saveHistory(H entry);

    Date getCurrentTimestamp();
}
