package com.dottydingo.hyperion.core.endpoint.pipeline.auth;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.core.model.PersistentObject;
import com.dottydingo.service.endpoint.context.UserContext;

/**
 */
public interface AuthorizationContext<U extends UserContext,C extends ApiObject, P extends PersistentObject>
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
     * @param persistent The persistent object
     * @param propertyName the API property name
     * @return True if the property is readable, false otherwise
     */
    boolean isReadable(P persistent, String propertyName);

    /**
     * Return a flag indicating if the supplied property is writable on a create
     * @param client The client object, may be sparsely populated
     * @param propertyName the API property name
     * @return True if the property is writable, false otherwise
     */
    boolean isWritableOnCreate(C client, String propertyName);

    /**
     * Return a flag indicating if the supplied property is writable on an update
     * @param client The client object, may be sparsely populated
     * @param persistent The persistent object
     * @param propertyName the API property name
     * @return True if the property is writable, false otherwise
     */
    boolean isWritableOnUpdate(C client, P persistent, String propertyName);
}
