package com.dottydingo.hyperion.api;

import java.io.Serializable;
import java.util.Date;

/**
 * An implementation of an auditable api object.
 */
public class BaseAuditableApiObject<ID extends Serializable> extends BaseApiObject<ID> implements AuditableApiObject<ID>
{
    private Date created;
    private String createdBy;
    private Date modified;
    private String modifiedBy;

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getCreated()
    {
        return created;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCreated(Date created)
    {
        this.created = created;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCreatedBy()
    {
        return createdBy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getModified()
    {
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setModified(Date modified)
    {
        this.modified = modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getModifiedBy()
    {
        return modifiedBy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setModifiedBy(String modifiedBy)
    {
        this.modifiedBy = modifiedBy;
    }
}
