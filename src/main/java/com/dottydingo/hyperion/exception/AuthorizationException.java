package com.dottydingo.hyperion.exception;

/**
 * User: mark
 * Date: 10/7/12
 * Time: 7:11 PM
 */
public class AuthorizationException extends ServiceException
{

    public AuthorizationException(String message)
    {
        super(403,message);
    }
}
