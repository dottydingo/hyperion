package com.dottydingo.hyperion.api.exception;

import com.dottydingo.hyperion.api.ErrorDetail;

import java.util.List;

/**
 * Base hyperion exception
 */
public class HyperionException extends RuntimeException
{
    private String requestId;
    private String errorTime;
    private int statusCode;
    private List<ErrorDetail> errorDetails;

    /**
     * Create a new exception using the supplied parameters. The status code will be 500.
     * @param message The error message
     */
    public HyperionException(String message)
    {
        this(500,message);
    }

    /**
     * Create a new exception using the supplied parameters
     * @param statusCode the status code
     * @param message The error message
     */
    public HyperionException(int statusCode, String message)
    {
        this(statusCode,message,null);
    }

    /**
     * Create a new exception using the supplied parameters
     * @param statusCode the status code
     * @param message The error message
     * @param cause the underlying cause
     */
    public HyperionException(int statusCode, String message, Throwable cause)
    {
        super(message,cause);
        this.statusCode = statusCode;
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
     * @param statusCode The status code
     */
    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    /**
     * Return the request ID
     * @return The request ID
     */
    public String getRequestId()
    {
        return requestId;
    }

    /**
     * Set the request ID
     * @param requestId The request ID
     */
    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
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
     * @param errorTime  the error time
     */
    public void setErrorTime(String errorTime)
    {
        this.errorTime = errorTime;
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
     * @param errorDetails THe error details
     */
    public void setErrorDetails(List<ErrorDetail> errorDetails)
    {
        this.errorDetails = errorDetails;
    }

    /**
     * Return a detail message. If errorDetails is not empty then the returned message will combine these together,
     * otherwise the standard exception message is returned.
     * @return The message
     */
    public String getDetailMessage()
    {
        if(errorDetails == null || errorDetails.isEmpty())
            return getMessage();

        StringBuilder sb = new StringBuilder(512);
        sb.append(getMessage());
        sb.append(" [");

        for (int i = 0; i < errorDetails.size(); i++)
        {
            ErrorDetail errorDetail = errorDetails.get(i);
            if(i > 0)
                sb.append(", ");
            sb.append(errorDetail.getMessage());
        }
        sb.append("]");

        return sb.toString();
    }
}
