package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.service.context.RequestContext;
import com.dottydingo.hyperion.service.model.PersistentObject;
import com.dottydingo.hyperion.service.query.Mapper;
import com.dottydingo.hyperion.service.query.PredicateBuilder;
import com.dottydingo.hyperion.service.query.RsqlPredicateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
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
    private Sort defaultSort = new Sort("id");
    private HyperionJpaRepository<P,ID> jpaRepository;
    private RsqlPredicateBuilder predicateBuilder;
    private Mapper mapper;
    private PersistenceFilter<P> persistenceFilter = new EmptyPersistenceFilter<P>();
    @PersistenceContext
    private EntityManager entityManager;

    public void setJpaRepository(HyperionJpaRepository<P, ID> jpaRepository)
    {
        this.jpaRepository = jpaRepository;
    }

    public void setPredicateBuilder(RsqlPredicateBuilder predicateBuilder)
    {
        this.predicateBuilder = predicateBuilder;
    }

    public void setMapper(Mapper mapper)
    {
        this.mapper = mapper;
    }

    public void setPersistenceFilter(PersistenceFilter<P> persistenceFilter)
    {
        this.persistenceFilter = persistenceFilter;
    }

    @Override
    public P findById(ID id, RequestContext context)
    {
        P persistent = jpaRepository.findOne(id);
        if(persistenceFilter.isVisible(persistent,context))
            return persistent;
        return null;
    }

    @Override
    public List<P> findByIds(List<ID> ids, RequestContext context)
    {
        Iterable<P> iterable = jpaRepository.findAll(ids);

        List<P> result = new ArrayList<P>();
        for (P p : iterable)
        {
            if(persistenceFilter.isVisible(p,context))
                result.add(p);
        }
        return result;
    }

    @Override
    public QueryResult<P> query(String query, Integer start, Integer limit, String sort, RequestContext context)
    {
        int size = limit == null ? 500 : limit;
        int page = start == null ? 0 : (start - 1) * size;
        Pageable pageable = new PageRequest(page,size,getSort(sort));

        List<Specification<P>> specificationList = new ArrayList<Specification<P>>();
        if(query != null && query.length() > 0)
        {
            specificationList.add(new QuerySpecification(predicateBuilder, query, getDomainType()));
        }

        Specification<P> filter = persistenceFilter.getFilterSpecification(context);
        if(filter != null)
            specificationList.add(filter);

        Specifications<P> specification = null;
        for (Specification<P> spec : specificationList)
        {
            if(specification == null)
                specification = Specifications.where(spec);
            else
                specification = specification.and(spec);
        }

        Page<P> all = jpaRepository.findAll(specification,pageable);

        List<P> list = all.getContent();

        QueryResult<P> queryResult= new QueryResult<P>();
        queryResult.setItems(list);
        queryResult.setResponseCount(list.size());
        queryResult.setTotalCount(list.size());
        queryResult.setStart(start == null ? 1 : (start));

        return queryResult;
    }

    @Override
    public P createItem(P item, RequestContext context)
    {
        if(persistenceFilter.canCreate(item,context))
            return jpaRepository.save(item);
        return null;
    }

    @Override
    public P updateItem(P item, RequestContext context)
    {
        if(persistenceFilter.canUpdate(item,context))
            return jpaRepository.save(item);
        return null;
    }

    @Override
    public int deleteItem(P item, RequestContext context)
    {
        if(persistenceFilter.canDelete(item,context))
        {
            jpaRepository.delete(item);
            return 1;
        }

        return 0;
    }

    private Class<?> getDomainType()
    {

        Class<?>[] arguments = resolveTypeArguments(jpaRepository.getClass(), Repository.class);
        return arguments == null ? null : arguments[0];
    }

    private Sort getSort(String sortString)
    {
        if (sortString == null || sortString.length() == 0)
        {
            return defaultSort;
        }

        Class entityType = getDomainType();

        boolean hasId = false;
        Sort sort = null;
        String[] split = sortString.split(",");
        for (String s1 : split)
        {
            String[] props = s1.split(":");
            String translated = mapper.translate(props[0].trim(), entityType);
            if(translated.equals("id"))
                hasId = true;
            boolean desc = props.length == 2 && props[1].equalsIgnoreCase("desc");
            Sort s = new Sort(desc ? Sort.Direction.DESC : Sort.Direction.ASC,translated);
            if (sort == null)
            {
                sort = s;
            }
            else
            {
                sort = sort.and(s);
            }
        }

        if(sort == null)
            sort = defaultSort;
        else if(!hasId)
            sort = sort.and(defaultSort);

        return sort;
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
