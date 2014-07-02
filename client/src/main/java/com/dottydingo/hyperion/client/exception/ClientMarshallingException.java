package com.dottydingo.hyperion.client.exception;

/**
 */
public class ClientMarshallingException extends ClientException
{
    public ClientMarshallingException(String message, Throwable throwable)
    {
        super(500, message, throwable);
    }
}
