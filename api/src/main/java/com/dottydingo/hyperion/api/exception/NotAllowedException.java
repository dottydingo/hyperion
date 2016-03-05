package com.dottydingo.hyperion.api.exception;

/**
 * A not allowed (405) error
 */
public class NotAllowedException extends HyperionException
{
    /**
     * Create a new exception using the supplied parameters
     * @param message The error message
     */
    public NotAllowedException(String message)
    {
        super(405,message);
    }
}
