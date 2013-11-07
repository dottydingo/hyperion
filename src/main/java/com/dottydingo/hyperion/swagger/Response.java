package com.dottydingo.hyperion.swagger;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 */
@JsonPropertyOrder({"code","message","responseModel"})
public class Response
{
    private Integer code;
    private String message;
    private String responseModel;

    public Integer getCode()
    {
        return code;
    }

    public void setCode(Integer code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getResponseModel()
    {
        return responseModel;
    }

    public void setResponseModel(String responseModel)
    {
        this.responseModel = responseModel;
    }
}
