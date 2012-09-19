package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.service.model.PersistentObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * User: mark
 * Date: 9/16/12
 * Time: 4:39 PM
 */
public interface HyperionJpaRepository<T extends PersistentObject, ID extends Serializable> extends
        JpaRepository<T,ID>, JpaSpecificationExecutor<T>
{
}
