package com.dottydingo.hyperion.client;

/**
 */
public interface AuthorizationFactory extends HeaderFactory,ParameterFactory
{
    boolean retryOnAuthenticationError();
}
