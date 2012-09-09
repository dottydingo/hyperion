package com.dottydingo.hyperion.api;

import java.io.Serializable;
import java.util.Date;

/**
 * User: mark
 * Date: 9/8/12
 * Time: 3:57 PM
 */
public class BaseAuditableApiObject<ID extends Serializable> extends BaseApiObject<ID>
{
    /**
     * The date the object was created (read only)
     */
    private Date created;

    /**
     * The user who created the object (read only)
     */
    private String createdBy;

    /**
     * The date the the object was last modified (read only)
     */
    private Date modified;

    /**
     * The user who last modified the object
     */
    private String modifiedBy;

    public Date getCreated()
    {
        return created;
    }

    public void setCreated(Date created)
    {
        this.created = created;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    public Date getModified()
    {
        return modified;
    }

    public void setModified(Date modified)
    {
        this.modified = modified;
    }

    public String getModifiedBy()
    {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy)
    {
        this.modifiedBy = modifiedBy;
    }
}
