package com.dottydingo.hyperion.client;

/**
 * An authorization factory that can be reset on an authorization error (401)
 */
public interface ResettableAuthorizationFactory extends AuthorizationFactory
{
    /**
     * Reset the authorization factory
     */
    void reset();
}
