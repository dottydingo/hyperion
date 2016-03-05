package com.dottydingo.hyperion.client;

/**
 * A factory for adding necessary authorization headers/parameters to a request
 */
public interface AuthorizationFactory extends HeaderFactory,ParameterFactory
{
    /**
     * Return a flag indicating if the client should retry on an authentication error (401)
     * @return True if the client should retry, false otherwise
     */
    boolean retryOnAuthenticationError();
}
