package com.dottydingo.hyperion.api.exception;

import com.dottydingo.hyperion.api.ErrorDetail;

import java.util.List;

/**
 */
public class HyperionException extends RuntimeException
{
    private int statusCode;
    private List<ErrorDetail> errorDetails;

    public HyperionException(String message)
    {
        this(500,message);
    }

    public HyperionException(int statusCode, String message)
    {
        this(statusCode,message,null);
    }

    public HyperionException(int statusCode, String message, Throwable throwable)
    {
        super(message,throwable);
        this.statusCode = statusCode;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    public List<ErrorDetail> getErrorDetails()
    {
        return errorDetails;
    }

    public void setErrorDetails(List<ErrorDetail> errorDetails)
    {
        this.errorDetails = errorDetails;
    }
}
