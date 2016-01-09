package com.dottydingo.hyperion.api.exception;

import com.dottydingo.hyperion.api.ErrorDetail;

import java.util.List;

/**
 */
public class HyperionException extends RuntimeException
{
    private String requestId;
    private String errorTime;
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

    public String getRequestId()
    {
        return requestId;
    }

    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }

    public String getErrorTime()
    {
        return errorTime;
    }

    public void setErrorTime(String errorTime)
    {
        this.errorTime = errorTime;
    }

    public List<ErrorDetail> getErrorDetails()
    {
        return errorDetails;
    }

    public void setErrorDetails(List<ErrorDetail> errorDetails)
    {
        this.errorDetails = errorDetails;
    }

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
