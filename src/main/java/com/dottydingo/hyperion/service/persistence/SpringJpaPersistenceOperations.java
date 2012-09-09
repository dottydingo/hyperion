package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.service.model.BasePersistentObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class SpringJpaPersistenceOperations<P extends BasePersistentObject, ID extends Serializable>
        implements PersistenceOperations<P,ID>
{
    private JpaRepository<P,ID> jpaRepository;

    public void setJpaRepository(JpaRepository<P, ID> jpaRepository)
    {
        this.jpaRepository = jpaRepository;
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
        Pageable pageable = new PageRequest(page,size);
        Page<P> all = jpaRepository.findAll(pageable);

        QueryResult<P> queryResult= new QueryResult<P>();
        queryResult.setItems(all.getContent());
        queryResult.setResponseCount(all.getNumberOfElements());
        queryResult.setTotalCount(all.getTotalElements());
        queryResult.setStart(start == null ? 0 : (start - 1));

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
}
