package com.dottydingo.hyperion.api.exception;

import com.dottydingo.hyperion.api.ErrorDetail;

import java.util.List;

/**
 * A conflict (409) error
 */
public class ConflictException extends HyperionException
{
    /**
     * Create a new exception using the supplied parameters
     * @param message The error message
     */
    public ConflictException(String message)
    {
        super(409,message);
    }

    /**
     * Create a new exception using the supplied parameters
     * @param message The error message
     * @param cause the underlying cause
     */
    public ConflictException(String message,Throwable cause)
    {
        super(409,message,cause);
    }

    /**
     * Create a new exception using the supplied parameters
     * @param message The error message
     * @param errorDetails details about the error
     */
    public ConflictException(String message,List<ErrorDetail> errorDetails)
    {
        super(409,message);
        setErrorDetails(errorDetails);
    }
}
