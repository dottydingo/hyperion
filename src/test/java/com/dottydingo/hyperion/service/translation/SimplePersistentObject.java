package com.dottydingo.hyperion.service.translation;

import com.dottydingo.hyperion.service.model.BaseAuditablePersistentObject;


/**
 */
public class SimplePersistentObject extends BaseAuditablePersistentObject<Long>
{
    private Long id;
    private String name;
    private Integer number;
    private String persistentOnly;
    private Integer differentType;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

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

    public String getPersistentOnly()
    {
        return persistentOnly;
    }

    public void setPersistentOnly(String persistentOnly)
    {
        this.persistentOnly = persistentOnly;
    }

    public Integer getDifferentType()
    {
        return differentType;
    }

    public void setDifferentType(Integer differentType)
    {
        this.differentType = differentType;
    }
}
