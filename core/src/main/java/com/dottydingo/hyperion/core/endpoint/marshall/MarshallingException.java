package com.dottydingo.hyperion.core.endpoint.marshall;

/**
 */
public class MarshallingException extends RuntimeException
{
    public MarshallingException(String message)
    {
        super(message);
    }

    public MarshallingException(Throwable cause)
    {
        super(cause);
    }
}
