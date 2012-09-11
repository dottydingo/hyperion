package com.dottydingo.hyperion.service.translation;


import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.service.model.PersistentObject;

import java.util.List;

/**
 */
public interface Translator<C extends ApiObject, P extends PersistentObject>
{
    P convertClient(C client, TranslationContext context);

    void copyClient(C client, P persistent, TranslationContext context);

    C convertPersistent(P persistent, TranslationContext context);

    List<C> convertPersistent(List<P> persistent, TranslationContext context);
}
