package com.dottydingo.hyperion.client.exception;

/**
 */
public class ClientConnectionException extends ClientException
{
    public ClientConnectionException(String message, Throwable throwable)
    {
        super(400, message, throwable);
    }
}
