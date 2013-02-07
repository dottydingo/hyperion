package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.service.model.PersistentObject;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.List;

/**
 * User: mark
 * Date: 1/27/13
 * Time: 9:38 AM
 */
public class JpaDao<P extends PersistentObject,ID extends Serializable> implements Dao<P,ID>
{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<P> findAll(Class<P> entityClass, List<ID> ids)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<P> query = cb.createQuery(entityClass);
        Root<P> root = query.from(entityClass);

        Path<?> path = root.get("id");
        Predicate predicate = path.in(cb.parameter(List.class, "ids"));


        query.where(predicate).orderBy()

        return null;
    }
}
