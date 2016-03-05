package com.dottydingo.hyperion.client;

/**
 * A factory that can be used to add additional parameters to the request.
 */
public interface ParameterFactory
{
    /**
     * Return the additional parameters to add to the request.
     * @return The parameters
     */
    MultiMap getParameters();
}
