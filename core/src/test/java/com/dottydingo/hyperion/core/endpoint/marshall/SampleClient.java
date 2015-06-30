package com.dottydingo.hyperion.core.endpoint.marshall;

import com.dottydingo.hyperion.api.BaseApiObject;

/**
 */
public class SampleClient extends BaseApiObject<Long>
{
    private String stringField;
    private String anotherStringField;

    public String getStringField()
    {
        return stringField;
    }

    public void setStringField(String stringField)
    {
        this.stringField = stringField;
    }

    public String getAnotherStringField()
    {
        return anotherStringField;
    }

    public void setAnotherStringField(String anotherStringField)
    {
        this.anotherStringField = anotherStringField;
    }
}
