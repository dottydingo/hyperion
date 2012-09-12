package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.service.model.PersistentObject;
import cz.jirutka.rsql.hibernate.RSQL2CriteriaConverter;
import cz.jirutka.rsql.hibernate.RSQL2CriteriaConverterImpl;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.core.GenericTypeResolver.resolveTypeArguments;

/**
 */
public class SpringJpaPersistenceOperations<P extends PersistentObject, ID extends Serializable>
        implements PersistenceOperations<P,ID>
{
    private JpaRepository<P,ID> jpaRepository;
    private RSQL2CriteriaConverter criteriaConverter;
    @PersistenceContext
    private EntityManager entityManager;

    public void setJpaRepository(JpaRepository<P, ID> jpaRepository)
    {
        this.jpaRepository = jpaRepository;
    }

    public void setCriteriaConverter(RSQL2CriteriaConverter criteriaConverter)
    {
        this.criteriaConverter = criteriaConverter;
    }

    @Override
    public P findById(ID id)
    {
        return jpaRepository.findOne(id);
    }

    @Override
    public List<P> findByIds(List<ID> ids)
    {
        Iterable<P> iterable = jpaRepository.findAll(ids);

        List<P> result = new ArrayList<P>();
        for (P p : iterable)
        {
            result.add(p);
        }
        return result;
    }

    @Override
    public QueryResult<P> query(String query, Integer start, Integer limit)
    {
        int size = limit == null ? 500 : limit;
        /*int page = start == null ? 0 : (start - 1) * size;
        Pageable pageable = new PageRequest(page,size);
        Page<P> all = jpaRepository.findAll(pageable);*/
        Criteria criteria = ((Session)entityManager.getDelegate()).createCriteria(getDomainType());

        if(query != null && query.length() > 0)
        {
            criteriaConverter.extendCriteria(query,getDomainType(), criteria);
        }

        criteria.setMaxResults(size);
        if(start != null)
            criteria.setFirstResult(start - 1);

        criteria.addOrder(Order.asc("id"));

        List<P> list = criteria.list();

        QueryResult<P> queryResult= new QueryResult<P>();
        queryResult.setItems(list);
        queryResult.setResponseCount(list.size());
        queryResult.setTotalCount(list.size());
        queryResult.setStart(start == null ? 1 : (start));

        return queryResult;
    }

    @Override
    public P createItem(P item)
    {
        return jpaRepository.save(item);
    }

    @Override
    public P updateItem(P item)
    {
        return jpaRepository.save(item);
    }

    @Override
    public void deleteItem(P item)
    {
        jpaRepository.delete(item);
    }

    private Class<?> getDomainType()
    {

        Class<?>[] arguments = resolveTypeArguments(jpaRepository.getClass(), Repository.class);
        return arguments == null ? null : arguments[0];
    }
}
