package com.dottydingo.hyperion.core.model;

import java.io.Serializable;
import java.util.Date;

/**
 * An entity containing audit fields.
 */
public interface AuditablePersistentObject<ID extends Serializable> extends PersistentObject<ID>
{
    /**
     * Return the date the entity was created.
     * @return The date
     */
    Date getCreated();

    /**
     * Set the date the entity was created
     * @param created The date
     */
    void setCreated(Date created);

    /**
     * Return the string representation of the user who created the entity
     * @return The user
     */
    String getCreatedBy();

    /**
     * Set the string representation of the user who created the entity
     * @param createdBy The user
     */
    void setCreatedBy(String createdBy);

    /**
     * Return the date the entity was last modified
     * @return The date
     */
    Date getModified();

    /**
     * Set the date the entity was last modified.
     * @param modified The date
     */
    void setModified(Date modified);

    /**
     * Return the string representation of the user who last modified the entity
     * @return The user
     */
    String getModifiedBy();

    /**
     * * Set the string representation of the user who last modified the entity
     * @param modifiedBy The user
     */
    void setModifiedBy(String modifiedBy);
}
