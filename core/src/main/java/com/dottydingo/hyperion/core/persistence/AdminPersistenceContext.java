package com.dottydingo.hyperion.core.persistence;

import com.dottydingo.hyperion.core.endpoint.pipeline.auth.NoOpAuthorizationContext;

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
