package com.dottydingo.hyperion.api.exception;

import com.dottydingo.hyperion.api.ErrorDetail;

import java.util.List;

/**
 * A validation (422) error
 */
public class ValidationException extends HyperionException
{
    /**
     * Create a new exception using the supplied parameters
     * @param message The error message
     */
    public ValidationException(String message)
    {
        super(422,message);
    }

    /**
     * Create a new exception using the supplied parameters
     * @param message The error message
     * @param errorDetails The error details
     */
    public ValidationException(String message,List<ErrorDetail> errorDetails)
    {
        super(422,message);
        setErrorDetails(errorDetails);
    }

    /**
     * Create a new exception using the supplied parameters
     * @param message The error message
     * @param cause the underlying cause
     */
    public ValidationException(String message, Throwable cause)
    {
        super(422,message,cause);
    }
}
