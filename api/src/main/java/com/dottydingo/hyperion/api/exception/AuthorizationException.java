package com.dottydingo.hyperion.api.exception;

/**
 * An authorization (403) error
 */
public class AuthorizationException extends HyperionException
{
    /**
     * Create a new exception using the supplied parameters
     * @param message The error message
     */
    public AuthorizationException(String message)
    {
        super(403,message);
    }
}
