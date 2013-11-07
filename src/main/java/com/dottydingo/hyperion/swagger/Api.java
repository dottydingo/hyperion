package com.dottydingo.hyperion.swagger;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 */
@JsonPropertyOrder({"path","description","operations"})
public class Api
{
    private String path;
    private String description;
    private List<Operation> operations;

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

    public List<Operation> getOperations()
    {
        return operations;
    }

    public void setOperations(List<Operation> operations)
    {
        this.operations = operations;
    }
}
