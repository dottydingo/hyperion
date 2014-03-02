package com.dottydingo.hyperion.exception;

/**
 */
public class ServiceUnavailableException extends HyperionException
{
    public ServiceUnavailableException(String message)
    {
        super(503, message);
    }
}
