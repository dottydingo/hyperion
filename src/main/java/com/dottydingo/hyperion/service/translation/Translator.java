package com.dottydingo.hyperion.service.translation;


import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.service.context.RequestContext;
import com.dottydingo.hyperion.service.model.PersistentObject;

import java.util.List;

/**
 */
public interface Translator<C extends ApiObject, P extends PersistentObject>
{
    P convertClient(C client, RequestContext context);

    void copyClient(C client, P persistent, RequestContext context);

    C convertPersistent(P persistent, RequestContext context);

    List<C> convertPersistent(List<P> persistent, RequestContext context);
}
