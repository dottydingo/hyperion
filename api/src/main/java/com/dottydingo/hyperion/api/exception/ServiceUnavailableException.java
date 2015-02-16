package com.dottydingo.hyperion.api.exception;

/**
 */
public class ServiceUnavailableException extends HyperionException
{
    public ServiceUnavailableException(String message)
    {
        super(503, message);
    }

    public ServiceUnavailableException(String message,Throwable cause)
    {
        super(503, message, cause);
    }
}

