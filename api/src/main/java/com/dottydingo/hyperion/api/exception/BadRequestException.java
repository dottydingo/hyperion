package com.dottydingo.hyperion.api.exception;

/**
 */
public class BadRequestException extends HyperionException
{
    public BadRequestException(String message)
    {
        super(400,message);
    }

    public BadRequestException(String message, Throwable cause)
    {
        super(400, message, cause );
    }
}
