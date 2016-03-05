package com.dottydingo.hyperion.api.exception;

/**
 * A bad request (400) error
 */
public class BadRequestException extends HyperionException
{
    /**
     * Create a new exception using the supplied parameters
     * @param message The error message
     */
    public BadRequestException(String message)
    {
        super(400,message);
    }

    /**
     * Create a new exception using the supplied parameters
     * @param message The error message
     * @param cause the underlying cause
     */
    public BadRequestException(String message, Throwable cause)
    {
        super(400, message, cause );
    }
}
