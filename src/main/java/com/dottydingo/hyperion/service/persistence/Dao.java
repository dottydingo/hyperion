package com.dottydingo.hyperion.service.persistence;

import java.io.Serializable;
import java.util.List;

/**
 * User: mark
 * Date: 1/27/13
 * Time: 9:30 AM
 */
public interface Dao<P,ID extends Serializable>
{
    List<P> findAll(Class<P> entityClass, List<ID> ids);
}
