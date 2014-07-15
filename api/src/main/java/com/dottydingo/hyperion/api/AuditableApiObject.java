package com.dottydingo.hyperion.api;

import java.io.Serializable;
import java.util.Date;

/**
 * An API object with audit fields.
 */
public interface AuditableApiObject<ID extends Serializable> extends ApiObject<ID>
{
    /**
     * Return the date the object was created.
     * @return The date
     */
    Date getCreated();

    /**
     * Set the date the object was created
     * @param created The date
     */
    void setCreated(Date created);

    /**
     * Return the string representation of the user who created the object
     * @return The user
     */
    String getCreatedBy();

    /**
     * Set the string representation of the user who created the object
     * @param createdBy The user
     */
    void setCreatedBy(String createdBy);

    /**
     * Return the date the object was last modified
     * @return The date
     */
    Date getModified();

    /**
     * Set the date the object was last modified.
     * @param modified The date
     */
    void setModified(Date modified);

    /**
     * Return the string representation of the user who last modified the object
     * @return The user
     */
    String getModifiedBy();

    /**
     * * Set the string representation of the user who last modified the object
     * @param modifiedBy The user
     */
    void setModifiedBy(String modifiedBy);
}
