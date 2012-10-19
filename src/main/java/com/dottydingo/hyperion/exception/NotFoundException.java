package com.dottydingo.hyperion.exception;

/**
 */
public class NotFoundException extends HyperionException
{
    public NotFoundException(String message)
    {
        super(404,message);
    }
}
