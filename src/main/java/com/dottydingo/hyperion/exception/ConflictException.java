package com.dottydingo.hyperion.exception;

/**
 */
public class ConflictException extends HyperionException
{
    public ConflictException(String message)
    {
        super(409,message);
    }
}
