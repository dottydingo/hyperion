package com.dottydingo.hyperion.client.exception;

/**
 * A client connection exception
 */
public class ClientConnectionException extends ClientException
{
    /**
     * Create a new exception using the supplied parameters
     * @param message The error message
     * @param cause the underlying cause
     */
    public ClientConnectionException(String message, Throwable cause)
    {
        super(400, message, cause);
    }
}
