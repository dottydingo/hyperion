package com.dottydingo.hyperion.service.persistence;

import com.dottydingo.hyperion.service.pipeline.auth.NoOpAuthorizationContext;

/**
 * A copied persistence context used for doing full translations to client objects for history and events.
 */
public class AdminPersistenceContext extends PersistenceContext
{

    public AdminPersistenceContext(PersistenceContext other)
    {
        super(other);
        setAuthorizationContext(new NoOpAuthorizationContext());
        setRequestedFields(null);
    }
}
