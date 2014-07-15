package com.dottydingo.hyperion.core.model;

import com.dottydingo.hyperion.api.HistoryAction;

import java.io.Serializable;
import java.util.Date;

/**
 */
public interface PersistentHistoryEntry<ID extends Serializable>
{
    Long getId();

    void setId(Long id);

    String getEntityType();

    void setEntityType(String entityType);

    ID getEntityId();

    void setEntityId(ID entityId);

    HistoryAction getHistoryAction();

    void setHistoryAction(HistoryAction historyAction);

    Integer getApiVersion();

    void setApiVersion(Integer apiVersion);

    String getSerializedEntry();

    void setSerializedEntry(String serializedEntry);

    String getUser();

    void setUser(String user);

    Date getTimestamp();

    void setTimestamp(Date timestamp);
}
