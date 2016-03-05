package com.dottydingo.hyperion.client.exception;

import com.dottydingo.hyperion.api.exception.HyperionException;

/**
 * A base client exception. Client exceptions incidate an error on the client side that prevents contacting the
 * server.
 */
public class ClientException extends HyperionException
{
    /**
     * Create a new exception using the supplied parameters
     * @param message The error message
     */
    public ClientException(String message)
    {
        super(message);
    }

    /**
     * Create a new exception using the supplied parameters
     * @param statusCode The status code
     * @param message The error message
     */
    public ClientException(int statusCode, String message)
    {
        super(statusCode, message);
    }

    /**
     * Create a new exception using the supplied parameters
     * @param statusCode The status code
     * @param message The error message
     * @param cause the underlying cause
     */
    public ClientException(int statusCode, String message, Throwable cause)
    {
        super(statusCode, message, cause);
    }
}
