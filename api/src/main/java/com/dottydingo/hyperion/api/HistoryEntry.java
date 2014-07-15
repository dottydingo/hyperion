package com.dottydingo.hyperion.api;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.Date;

/**
 */
@JsonPropertyOrder({"id","apiVersion","historyAction","entry","user","timestamp"})
public class HistoryEntry<ID extends Serializable, T extends ApiObject<ID>>
{
    private ID id;
    private Integer apiVersion;
    private T entry;
    private HistoryAction historyAction;
    private String user;
    private Date timestamp;

    public ID getId()
    {
        return id;
    }

    public void setId(ID id)
    {
        this.id = id;
    }

    public Integer getApiVersion()
    {
        return apiVersion;
    }

    public void setApiVersion(Integer apiVersion)
    {
        this.apiVersion = apiVersion;
    }

    public T getEntry()
    {
        return entry;
    }

    public void setEntry(T entry)
    {
        this.entry = entry;
    }

    public HistoryAction getHistoryAction()
    {
        return historyAction;
    }

    public void setHistoryAction(HistoryAction historyAction)
    {
        this.historyAction = historyAction;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }
}
