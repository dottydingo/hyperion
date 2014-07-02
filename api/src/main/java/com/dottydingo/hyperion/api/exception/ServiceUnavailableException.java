package com.dottydingo.hyperion.api.exception;

/**
 */
public class ServiceUnavailableException extends HyperionException
{
    public ServiceUnavailableException(String message)
    {
        super(503, message);
    }
}
