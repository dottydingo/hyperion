package com.dottydingo.hyperion.exception;

import com.dottydingo.hyperion.api.ErrorDetail;

import java.util.List;

/**
 */
public class ValidationException extends HyperionException
{
    public ValidationException(String message)
    {
        super(422,message);
    }

    public ValidationException(String message,List<ErrorDetail> errorDetails)
    {
        super(422,message);
        setErrorDetails(errorDetails);
    }

    public ValidationException(String message, Throwable cause)
    {
        super(422,message,cause);
    }
}
