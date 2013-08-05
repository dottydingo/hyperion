package com.dottydingo.hyperion.service.validation;

/**
*/
public class ValidationErrorHolder
{
    private String resourceCode;
    private String fieldName;
    private Object[] parameters;

    ValidationErrorHolder(String resourceCode, String fieldName, Object[] parameters)
    {
        this.resourceCode = resourceCode;
        this.fieldName = fieldName;
        this.parameters = parameters;
    }

    public String getResourceCode()
    {
        return resourceCode;
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public Object[] getParameters()
    {
        return parameters;
    }
}
