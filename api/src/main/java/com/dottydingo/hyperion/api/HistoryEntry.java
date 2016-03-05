package com.dottydingo.hyperion.api;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.Date;

/**
 * A history entry
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

    /**
     * Return the ID of the item
     * @return The id
     */
    public ID getId()
    {
        return id;
    }

    /**
     * Set the ID fo the item
     * @param id The ID
     */
    public void setId(ID id)
    {
        this.id = id;
    }

    /**
     * Return the API version
     * @return THe API version
     */
    public Integer getApiVersion()
    {
        return apiVersion;
    }

    /**
     * Set the API version
     * @param apiVersion  The api version
     */
    public void setApiVersion(Integer apiVersion)
    {
        this.apiVersion = apiVersion;
    }

    /**
     * Return the entry
     * @return The entry
     */
    public T getEntry()
    {
        return entry;
    }

    /**
     * Set the entry
     * @param entry the entry
     */
    public void setEntry(T entry)
    {
        this.entry = entry;
    }

    /**
     * Return the history action
     * @return The action
     */
    public HistoryAction getHistoryAction()
    {
        return historyAction;
    }

    /**
     * Set the history action
     * @param historyAction  the action
     */
    public void setHistoryAction(HistoryAction historyAction)
    {
        this.historyAction = historyAction;
    }

    /**
     * Return the user
     * @return The user
     */
    public String getUser()
    {
        return user;
    }

    /**
     * Se the user
     * @param user The user
     */
    public void setUser(String user)
    {
        this.user = user;
    }

    /**
     * Return the timestamp
     * @return the timestamp
     */
    public Date getTimestamp()
    {
        return timestamp;
    }

    /**
     * Set the timestamp
     * @param timestamp the timestamp
     */
    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }
}
