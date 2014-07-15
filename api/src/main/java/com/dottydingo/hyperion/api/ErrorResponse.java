package com.dottydingo.hyperion.api;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 *
 */
@JsonPropertyOrder({"statusCode","type","message","errorDetail"})
public class ErrorResponse
{
    private int statusCode;
    private String message;
    private String type;
    private List<ErrorDetail> errorDetails;
    private String stackTrace;

    public int getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public List<ErrorDetail> getErrorDetails()
    {
        return errorDetails;
    }

    public void setErrorDetails(List<ErrorDetail> errorDetails)
    {
        this.errorDetails = errorDetails;
    }

    public String getStackTrace()
    {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace)
    {
        this.stackTrace = stackTrace;
    }
}
