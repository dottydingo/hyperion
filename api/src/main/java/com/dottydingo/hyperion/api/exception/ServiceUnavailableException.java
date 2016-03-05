package com.dottydingo.hyperion.api.exception;

/**
 * A service unavailable (503) error
 */
public class ServiceUnavailableException extends HyperionException
{
    /**
     * Create a new exception using the supplied parameters
     * @param message The error message
     */
    public ServiceUnavailableException(String message)
    {
        super(503, message);
    }

    /**
     * Create a new exception using the supplied parameters
     * @param message The error message
     * @param cause the underlying cause
     */
    public ServiceUnavailableException(String message,Throwable cause)
    {
        super(503, message, cause);
    }
}

