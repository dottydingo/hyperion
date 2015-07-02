package com.dottydingo.hyperion.core.endpoint.marshall;

/**
 */
public class WriteLimitException extends MarshallingException
{
    private int writeLimit;

    public WriteLimitException(int writeLimit)
    {
        this.writeLimit = writeLimit;
    }

    public int getWriteLimit()
    {
        return writeLimit;
    }
}
