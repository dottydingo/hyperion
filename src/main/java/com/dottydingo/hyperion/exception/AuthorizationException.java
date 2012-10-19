package com.dottydingo.hyperion.exception;

/**
 */
public class AuthorizationException extends HyperionException
{

    public AuthorizationException(String message)
    {
        super(403,message);
    }
}
