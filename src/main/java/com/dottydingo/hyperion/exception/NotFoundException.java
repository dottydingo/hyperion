package com.dottydingo.hyperion.exception;

/**
 * User: mark
 * Date: 9/19/12
 * Time: 8:20 PM
 */
public class NotFoundException extends ServiceException
{
    public NotFoundException(String message)
    {
        super(404,message);
    }
}
