package com.dottydingo.hyperion.api.exception;

/**
 */
public class InternalException extends HyperionException
{
    public InternalException(String message)
    {
        super(500,message);
    }

    public InternalException(String message, Throwable cause)
    {
        super(500,message,cause);
    }

}
