package com.dottydingo.hyperion.core.validation;

import com.dottydingo.hyperion.core.persistence.PersistenceContext;

/**
 */
public interface Validator<C,P>
{
    void validateCreate(C clientObject, PersistenceContext persistenceContext);

    void validateUpdate(C clientObject, P persistentObject, PersistenceContext persistenceContext);

    void validateDelete(P persistentObject, PersistenceContext persistenceContext);
}
