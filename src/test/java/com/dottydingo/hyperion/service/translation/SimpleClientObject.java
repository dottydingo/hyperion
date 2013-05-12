package com.dottydingo.hyperion.service.translation;

import com.dottydingo.hyperion.api.BaseApiObject;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 */
public class SimpleClientObject extends BaseApiObject<Long>
{
    private String name;
    private Integer number;
    private String clientOnly;
    private String differentType;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getNumber()
    {
        return number;
    }

    public void setNumber(Integer number)
    {
        this.number = number;
    }

    public String getClientOnly()
    {
        return clientOnly;
    }

    public void setClientOnly(String clientOnly)
    {
        this.clientOnly = clientOnly;
    }

    @JsonProperty("dt")
    public String getDifferentType()
    {
        return differentType;
    }

    public void setDifferentType(String differentType)
    {
        this.differentType = differentType;
    }
}
