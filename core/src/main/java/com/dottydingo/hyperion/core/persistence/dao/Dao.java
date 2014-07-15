package com.dottydingo.hyperion.core.persistence.dao;
;
import com.dottydingo.hyperion.core.model.PersistentHistoryEntry;
import com.dottydingo.hyperion.core.persistence.query.PersistentQueryBuilder;
import com.dottydingo.hyperion.core.persistence.sort.PersistentOrderBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * User: mark
 * Date: 1/27/13
 * Time: 9:30 AM
 */
public interface Dao<P,ID extends Serializable,QB extends PersistentQueryBuilder,SB extends PersistentOrderBuilder>
{
    List<P> findAll(Class<P> entityClass, List<ID> ids);

    PersistentQueryResult<P> query(Class<P> entityClass, Integer start, Integer limit, SB orderBuilder,
                  List<QB> predicateBuilders);

    P find(Class<P> entityClass, ID id);

    P create(P entity);

    P update(P entity);

    void delete(P entity);

    P reset(P entity);

    <H extends PersistentHistoryEntry<ID>> PersistentQueryResult<H> getHistory(Class<H> historyType, String entityType, ID entityId, Integer start,
                                                                  Integer limit);

    <H extends PersistentHistoryEntry<ID>> void saveHistory(H entry);

    Date getCurrentTimestamp();
}
