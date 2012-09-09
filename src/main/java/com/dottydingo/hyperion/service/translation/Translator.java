package com.dottydingo.hyperion.service.translation;


import com.dottydingo.hyperion.api.BaseApiObject;
import com.dottydingo.hyperion.service.model.BasePersistentObject;

import java.util.List;

/**
 */
public interface Translator<C extends BaseApiObject, P extends BasePersistentObject>
{
    P convertClient(C client, TranslationContext context);

    void copyClient(C client, P persistent, TranslationContext context);

    C convertPersistent(P persistent, TranslationContext context);

    List<C> convertPersistent(List<P> persistent, TranslationContext context);
}
