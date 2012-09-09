package com.dottydingo.hyperion.service.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 */
public class DefaultQueryHandler<T> implements QueryHandler<T>
{
    private JpaRepository<T,Long> jpaRepository;

    public void setJpaRepository(JpaRepository<T, Long> jpaRepository)
    {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Page<T> processQuery(String query, Integer start, Integer limit)
    {
        int page = start == null ? 0 : start - 1;
        int size = limit == null ? 500 : limit;
        Pageable pageable = new PageRequest(page,size);
        return jpaRepository.findAll(pageable);
    }
}
