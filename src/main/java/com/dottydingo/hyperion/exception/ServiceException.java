package com.dottydingo.hyperion.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 */
public class ServiceException extends RuntimeException
{
    private int statusCode;

    public ServiceException(String message)
    {
        this(500,message);
    }

    public ServiceException(int statusCode, String message)
    {
        this(statusCode,message,null);
    }

    public ServiceException(int statusCode, String message, Throwable throwable)
    {
        super(message,throwable);
        this.statusCode = statusCode;
    }

    public int getStatusCode()
    {
        return statusCode;
    }
}
