package com.dottydingo.hyperion.exception;

/**
 */
public class ValidationException extends HyperionException
{
    public ValidationException(String message)
    {
        super(422,message);
    }

    public ValidationException(String message, Throwable cause)
    {
        super(422,message,cause);
    }
}
