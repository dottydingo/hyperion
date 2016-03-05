package com.dottydingo.hyperion.client.exception;

/**
 * A client side error caused by a marshalling problem
 */
public class ClientMarshallingException extends ClientException
{
    /**
     * Create a new exception using the supplied parameters
     * @param message The error message
     */
    public ClientMarshallingException(String message, Throwable throwable)
    {
        super(500, message, throwable);
    }
}
