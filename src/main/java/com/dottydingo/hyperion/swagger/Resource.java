package com.dottydingo.hyperion.swagger;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 */
@JsonPropertyOrder({"path","description"})
public class Resource
{
    private String path;
    private String description;

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
