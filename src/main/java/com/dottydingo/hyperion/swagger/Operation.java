package com.dottydingo.hyperion.swagger;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 */
@JsonPropertyOrder({"method","nickname","type","parameters","summary","notes","errorResponses"})
public class Operation
{
    private String method;
    private String nickname;
    private String type;
    private List<Parameter> parameters;
    private String summary;
    private String notes;
    private List<Response> errorResponses;

    public String getMethod()
    {
        return method;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public List<Parameter> getParameters()
    {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters)
    {
        this.parameters = parameters;
    }

    public String getSummary()
    {
        return summary;
    }

    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public List<Response> getErrorResponses()
    {
        return errorResponses;
    }

    public void setErrorResponses(List<Response> errorResponses)
    {
        this.errorResponses = errorResponses;
    }
}
