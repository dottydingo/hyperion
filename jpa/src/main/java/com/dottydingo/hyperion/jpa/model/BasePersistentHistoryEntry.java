package com.dottydingo.hyperion.jpa.model;

import com.dottydingo.hyperion.api.HistoryAction;
import com.dottydingo.hyperion.core.model.PersistentHistoryEntry;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 */
@MappedSuperclass
public class BasePersistentHistoryEntry<ID extends Serializable> implements PersistentHistoryEntry<ID>
{
    private Long id;
    private String entityType;
    private ID entityId;
    private HistoryAction historyAction;
    private Integer apiVersion;
    private String serializedEntry;
    private String user;
    private Date timestamp;

    @Override
    @Transient
    public Long getId()
    {
        return id;
    }

    @Override
    public void setId(Long id)
    {
        this.id = id;
    }

    @Override
    @Transient
    public String getEntityType()
    {
        return entityType;
    }

    @Override
    public void setEntityType(String entityType)
    {
        this.entityType = entityType;
    }

    @Override
    @Transient
    public ID getEntityId()
    {
        return entityId;
    }

    @Override
    public void setEntityId(ID entityId)
    {
        this.entityId = entityId;
    }

    @Override
    @Transient
    public HistoryAction getHistoryAction()
    {
        return historyAction;
    }

    @Override
    public void setHistoryAction(HistoryAction historyAction)
    {
        this.historyAction = historyAction;
    }

    @Override
    @Transient
    public Integer getApiVersion()
    {
        return apiVersion;
    }

    @Override
    public void setApiVersion(Integer apiVersion)
    {
        this.apiVersion = apiVersion;
    }

    @Override
    @Transient
    public String getSerializedEntry()
    {
        return serializedEntry;
    }

    @Override
    public void setSerializedEntry(String serializedEntry)
    {
        this.serializedEntry = serializedEntry;
    }

    @Override
    @Transient
    public String getUser()
    {
        return user;
    }

    @Override
    public void setUser(String user)
    {
        this.user = user;
    }

    @Override
    @Transient
    public Date getTimestamp()
    {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }
}

