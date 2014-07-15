package com.dottydingo.hyperion.api.exception;

/**
 */
public class NotAllowedException extends HyperionException
{
    public NotAllowedException(String message)
    {
        super(405,message);
    }
}
