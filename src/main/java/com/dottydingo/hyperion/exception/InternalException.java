package com.dottydingo.hyperion.exception;

/**
 * User: mark
 * Date: 9/19/12
 * Time: 8:26 PM
 */
public class InternalException extends ServiceException
{
    public InternalException(String message)
    {
        super(500,message);
    }
}
