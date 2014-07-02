package com.dottydingo.hyperion.client.exception;

import com.dottydingo.hyperion.api.exception.HyperionException;

/**
 */
public class ClientException extends HyperionException
{
    public ClientException(String message)
    {
        super(message);
    }

    public ClientException(int statusCode, String message)
    {
        super(statusCode, message);
    }

    public ClientException(int statusCode, String message, Throwable throwable)
    {
        super(statusCode, message, throwable);
    }
}
