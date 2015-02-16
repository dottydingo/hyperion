package com.dottydingo.hyperion.api.exception;

import com.dottydingo.hyperion.api.ErrorDetail;

import java.util.List;

/**
 */
public class ConflictException extends HyperionException
{
    public ConflictException(String message)
    {
        super(409,message);
    }

    public ConflictException(String message,Throwable cause)
    {
        super(409,message,cause);
    }

    public ConflictException(String message,List<ErrorDetail> errorDetails)
    {
        super(409,message);
        setErrorDetails(errorDetails);
    }
}
