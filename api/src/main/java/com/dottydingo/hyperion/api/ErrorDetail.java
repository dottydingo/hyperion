package com.dottydingo.hyperion.api;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Error details
 */
@JsonPropertyOrder({"code","message"})
public class ErrorDetail
{
    private String code;
    private String message;

    /**
     * Return the code
     * @return The code
     */
    public String getCode()
    {
        return code;
    }

    /**
     * Set the code
     * @param code The code
     */
    public void setCode(String code)
    {
        this.code = code;
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
}
