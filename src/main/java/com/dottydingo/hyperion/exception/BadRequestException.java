package com.dottydingo.hyperion.exception;

/**
 * User: mark
 * Date: 9/16/12
 * Time: 11:26 AM
 */
public class BadRequestException extends ServiceException
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
