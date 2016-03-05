package com.dottydingo.hyperion.api;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * An error response
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

    /**
     * Return the request ID
     * @return The request id
     */
    public String getRequestId()
    {
        return requestId;
    }

    /**
     * Set the request ID
     * @param requestId The request id
     */
    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }

    /**
     * Return the status code
     * @return The status code
     */
    public int getStatusCode()
    {
        return statusCode;
    }

    /**
     * Set the status code
     * @param statusCode  The status code
     */
    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    /**
     * Return the error time
     * @return The error time
     */
    public String getErrorTime()
    {
        return errorTime;
    }

    /**
     * Set the error time
     * @param errorTime The error time
     */
    public void setErrorTime(String errorTime)
    {
        this.errorTime = errorTime;
    }

    /**
     * Return the message
     * @return The message
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * Set the message
     * @param message The message
     */
    public void setMessage(String message)
    {
        this.message = message;
    }

    /**
     * Return the error type
     * @return The error type
     */
    public String getType()
    {
        return type;
    }

    /**
     * Set the error type
     * @param type The error type
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * Return the error details
     * @return The error details
     */
    public List<ErrorDetail> getErrorDetails()
    {
        return errorDetails;
    }

    /**
     * Set the error details
     * @param errorDetails The error details
     */
    public void setErrorDetails(List<ErrorDetail> errorDetails)
    {
        this.errorDetails = errorDetails;
    }

    /**
     * Return the stack trace
     * @return The stack trace
     */
    public String getStackTrace()
    {
        return stackTrace;
    }

    /**
     * Set the stack track
     * @param stackTrace  THe stack trace
     */
    public void setStackTrace(String stackTrace)
    {
        this.stackTrace = stackTrace;
    }
}
