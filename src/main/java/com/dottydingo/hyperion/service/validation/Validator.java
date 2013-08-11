package com.dottydingo.hyperion.service.validation;

import com.dottydingo.hyperion.service.persistence.PersistenceContext;

/**
 */
public interface Validator<C,P>
{
    void validateCreate(C clientObject, PersistenceContext persistenceContext);

    void validateUpdate(C clientObject, P persistentObject, PersistenceContext persistenceContext);

    void validateDelete(P persistentObject, PersistenceContext persistenceContext);
}
