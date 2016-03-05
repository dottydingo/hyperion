package com.dottydingo.hyperion.api.exception;

/**
 * A bad parameter (400) error
 */
public class BadParameterException extends BadRequestException
{
    /**
     * Create a new exception using the supplied parameters
     * @param message The error message
     */
    public BadParameterException(String message)
    {
        super(message);
    }

    /**
     * Create a new exception using the supplied parameters
     * @param message The error message
     * @param cause the underlying cause
     */
    public BadParameterException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
