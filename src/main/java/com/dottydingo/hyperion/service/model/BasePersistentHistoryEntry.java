package com.dottydingo.hyperion.service.model;

import com.dottydingo.hyperion.service.endpoint.HistoryAction;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 */
@MappedSuperclass
public class BasePersistentHistoryEntry<ID extends Serializable>
{
    private Long id;
    private String entityType;
    private ID entityId;
    private HistoryAction historyAction;
    private Integer apiVersion;
    private String serializedEntry;
    private String user;
    private Date timestamp;

    @Transient
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @Transient
    public String getEntityType()
    {
        return entityType;
    }

    public void setEntityType(String entityType)
    {
        this.entityType = entityType;
    }

    @Transient
    public ID getEntityId()
    {
        return entityId;
    }

    public void setEntityId(ID entityId)
    {
        this.entityId = entityId;
    }

    @Transient
    public HistoryAction getHistoryAction()
    {
        return historyAction;
    }

    public void setHistoryAction(HistoryAction historyAction)
    {
        this.historyAction = historyAction;
    }

    @Transient
    public Integer getApiVersion()
    {
        return apiVersion;
    }

    public void setApiVersion(Integer apiVersion)
    {
        this.apiVersion = apiVersion;
    }

    @Transient
    public String getSerializedEntry()
    {
        return serializedEntry;
    }

    public void setSerializedEntry(String serializedEntry)
    {
        this.serializedEntry = serializedEntry;
    }

    @Transient
    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    @Transient
    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }
}

