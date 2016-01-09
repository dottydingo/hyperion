package com.dottydingo.hyperion.core.endpoint.pipeline.auth;

import com.dottydingo.service.endpoint.context.UserContext;

/**
 */
public interface AuthorizationContext<U extends UserContext>
{
    /**
     * Return the user context associated with this authorization context
     * @return The user context
     */
    U getUserContext();

    /**
     * Flag indicating if the user is authorized to perform the requested action
     * @return True if the user is authorized, false otherwise
     */
    boolean isAuthorized();

    /**
     * Return a flag indicating if the supplied property is readable
     * @param propertyName the property
     * @return True if the property is readable, false otherwise
     */
    boolean isReadable(String propertyName);

    /**
     * Return a flag indicating if the supplied property is writable
     * @param propertyName the property
     * @return True if the property is writable, false otherwise
     */
    boolean isWritable(String propertyName);
}
