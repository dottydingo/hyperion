package com.dottydingo.hyperion.service.context;

/**
 */
public enum HttpMethod
{
    GET(false),
    POST(true),
    PUT(true),
    DELETE(true),
    HEAD(false),
    OPTIONS(false);

    private boolean writeOperation;

    private HttpMethod(boolean writeOperation)
    {
        this.writeOperation = writeOperation;
    }

    public boolean isWriteOperation()
    {
        return writeOperation;
    }
}
