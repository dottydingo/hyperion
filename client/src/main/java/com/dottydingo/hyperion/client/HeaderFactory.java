package com.dottydingo.hyperion.client;

/**
 * A factory that can be used to add additional headers to the request.
 */
public interface HeaderFactory
{
    /**
     * Return the additional headers to add to the request.
     * @return The headers
     */
    MultiMap getHeaders();
}
