package com.dottydingo.hyperion.swagger;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;

import java.util.Map;

/**
 */
@JsonPropertyOrder({"id","properties"})
public class Model
{
    private String id;
    private Map<String, JsonSchema> properties;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Map<String, JsonSchema> getProperties()
    {
        return properties;
    }

    public void setProperties(Map<String, JsonSchema> properties)
    {
        this.properties = properties;
    }
}
