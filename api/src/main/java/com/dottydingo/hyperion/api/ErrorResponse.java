package com.dottydingo.hyperion.api;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 *
 */
@JsonPropertyOrder({"requestId","statusCode","type","errorTime","message","errorDetail","stackTrace"})
public class ErrorResponse
{
    private String requestId;
    private int statusCode;
    private String errorTime;
    private String message;
    private String type;
    private List<ErrorDetail> errorDetails;
    private String stackTrace;

    public String getRequestId()
    {
        return requestId;
    }

    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    public String getErrorTime()
    {
        return errorTime;
    }

    public void setErrorTime(String errorTime)
    {
        this.errorTime = errorTime;
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
