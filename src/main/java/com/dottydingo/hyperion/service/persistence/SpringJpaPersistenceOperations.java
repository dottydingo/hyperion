package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.service.model.PersistentObject;
import com.dottydingo.hyperion.service.query.PredicateBuilder;
import com.dottydingo.hyperion.service.query.RsqlPredicateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.core.GenericTypeResolver.resolveTypeArguments;

/**
 */
public class SpringJpaPersistenceOperations<P extends PersistentObject, ID extends Serializable>
        implements PersistenceOperations<P,ID>
{
    private HyperionJpaRepository<P,ID> jpaRepository;
    //private RSQL2CriteriaConverter criteriaConverter;
    private RsqlPredicateBuilder predicateBuilder;
    @PersistenceContext
    private EntityManager entityManager;

    public void setJpaRepository(HyperionJpaRepository<P, ID> jpaRepository)
    {
        this.jpaRepository = jpaRepository;
    }

    /*public void setCriteriaConverter(RSQL2CriteriaConverter criteriaConverter)
    {
        this.criteriaConverter = criteriaConverter;
    }*/

    public void setPredicateBuilder(RsqlPredicateBuilder predicateBuilder)
    {
        this.predicateBuilder = predicateBuilder;
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
        int page = start == null ? 0 : (start - 1) * size;
        Pageable pageable = new PageRequest(page,size,new Sort("id"));


        Page<P> all = null;

        if(query != null && query.length() > 0)
        {
            all = jpaRepository.findAll(new QuerySpecification(predicateBuilder,query,getDomainType()),pageable);
        }
        else
        {
            all = jpaRepository.findAll(pageable);
        }

        List<P> list = all.getContent();
        /*Criteria criteria = ((Session)entityManager.getDelegate()).createCriteria(getDomainType());

     if(query != null && query.length() > 0)
     {
         criteriaConverter.extendCriteria(query,getDomainType(), criteria);
     }

     criteria.setMaxResults(size);
     if(start != null)
         criteria.setFirstResult(start - 1);

     criteria.addOrder(Order.asc("id"));

     List<P> list = criteria.list();*/

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

    private class QuerySpecification<T> implements Specification<T>
    {
        private PredicateBuilder predicateBuilder;
        private String queryString;
        private Class<T> entityClass;

        private QuerySpecification(PredicateBuilder predicateBuilder, String queryString, Class<T> entityClass)
        {
            this.predicateBuilder = predicateBuilder;
            this.queryString = queryString;
            this.entityClass = entityClass;
        }

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb)
        {
            return predicateBuilder.buildPredicate(queryString,entityClass,root,cb);
        }
    }
}
