package com.dottydingo.hyperion.api.exception;

/**
 * A not found (404) error
 */
public class NotFoundException extends HyperionException
{
    /**
     * Create a new exception using the supplied parameters
     * @param message The error message
     */
    public NotFoundException(String message)
    {
        super(404,message);
    }
}
