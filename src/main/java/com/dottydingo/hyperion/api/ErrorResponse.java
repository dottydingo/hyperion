package com.dottydingo.hyperion.api;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 *
 */
@JsonPropertyOrder({"statusCode","type","message","errorDetail"})
public class ErrorResponse
{
    private int statusCode;
    private String message;
    private String type;
    private String errorDetail;

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

    public String getErrorDetail()
    {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail)
    {
        this.errorDetail = errorDetail;
    }
}
