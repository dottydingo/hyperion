package com.dottydingo.hyperion.exception;

/**
 */
public class NotAllowedException extends HyperionException
{
    public NotAllowedException(String message)
    {
        super(405,message);
    }
}
