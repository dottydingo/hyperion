package com.dottydingo.hyperion.service.exception;

/**
 * User: mark
 * Date: 9/16/12
 * Time: 11:26 AM
 */
public class QueryException extends ServiceException
{
    public QueryException(String message)
    {
        super(400,message);
    }

    public QueryException(String message, Throwable cause)
    {
        super(400, message, cause );
    }
}
