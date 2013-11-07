package com.dottydingo.hyperion.service.model;

import com.dottydingo.hyperion.api.HistoryAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 */
@MappedSuperclass()
public class DefaultPersistentHistoryEntry<ID extends Serializable> extends BasePersistentHistoryEntry<ID>
{
    @Id()
    @GeneratedValue
    @Column(name = "entity_history_id")
    @Override
    public Long getId()
    {
        return super.getId();
    }

    @Column(name = "entity_type")
    @Override
    public String getEntityType()
    {
        return super.getEntityType();
    }

    @Column(name = "entity_id")
    @Override
    public ID getEntityId()
    {
        return super.getEntityId();
    }

    @Column(name = "history_action")
    @Enumerated(EnumType.STRING)
    @Override
    public HistoryAction getHistoryAction()
    {
        return super.getHistoryAction();
    }

    @Column(name = "api_version")
    @Override
    public Integer getApiVersion()
    {
        return super.getApiVersion();
    }

    @Column(name = "serialized_entry")
    @Override
    public String getSerializedEntry()
    {
        return super.getSerializedEntry();
    }

    @Column(name = "user")
    @Override
    public String getUser()
    {
        return super.getUser();
    }

    @Column(name = "timestamp")
    @Override
    public Date getTimestamp()
    {
        return super.getTimestamp();
    }
}
