package com.dottydingo.hyperion.exception;

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

    public ConflictException(String message,List<ErrorDetail> errorDetails)
    {
        super(422,message);
        setErrorDetails(errorDetails);
    }
}
