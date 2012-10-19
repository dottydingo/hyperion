package com.dottydingo.hyperion.exception;

/**
 */
public class HyperionException extends RuntimeException
{
    private int statusCode;

    public HyperionException(String message)
    {
        this(500,message);
    }

    public HyperionException(int statusCode, String message)
    {
        this(statusCode,message,null);
    }

    public HyperionException(int statusCode, String message, Throwable throwable)
    {
        super(message,throwable);
        this.statusCode = statusCode;
    }

    public int getStatusCode()
    {
        return statusCode;
    }
}
