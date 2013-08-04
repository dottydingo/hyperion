package com.dottydingo.hyperion.service.translation;


import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.service.persistence.PersistenceContext;
import com.dottydingo.hyperion.service.model.PersistentObject;

import java.util.List;

/**
 */
public interface Translator<C extends ApiObject, P extends PersistentObject>
{
    P convertClient(C client, PersistenceContext context);

    boolean copyClient(C client, P persistent, PersistenceContext context);

    C convertPersistent(P persistent, PersistenceContext context);

    List<C> convertPersistent(List<P> persistent, PersistenceContext context);
}
