package com.dottydingo.hyperion.swagger;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Map;

/**
 */
@JsonPropertyOrder({"apiVersion","swaggerVersion","basePath","resourcePath","apis","models"})
public class ApiDeclaration
{
    private String apiVersion;
    private String swaggerVersion;
    private String basePath;
    private String resourcePath;
    private List<Api> apis;
    private Map<String,Model> models;

    public String getApiVersion()
    {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion)
    {
        this.apiVersion = apiVersion;
    }

    public String getSwaggerVersion()
    {
        return swaggerVersion;
    }

    public void setSwaggerVersion(String swaggerVersion)
    {
        this.swaggerVersion = swaggerVersion;
    }

    public String getBasePath()
    {
        return basePath;
    }

    public void setBasePath(String basePath)
    {
        this.basePath = basePath;
    }

    public String getResourcePath()
    {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath)
    {
        this.resourcePath = resourcePath;
    }

    public List<Api> getApis()
    {
        return apis;
    }

    public void setApis(List<Api> apis)
    {
        this.apis = apis;
    }

    public Map<String, Model> getModels()
    {
        return models;
    }

    public void setModels(Map<String, Model> models)
    {
        this.models = models;
    }
}
