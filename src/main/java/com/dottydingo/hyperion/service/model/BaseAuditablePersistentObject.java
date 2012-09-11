package com.dottydingo.hyperion.service.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 */
@MappedSuperclass
public abstract class BaseAuditablePersistentObject<ID extends Serializable> extends BasePersistentObject<ID>
        implements AuditablePersistentObject<ID>
{

    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "modified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;
    @Column(name = "modified_by")
    private String modifiedBy;

    @Override
    public Date getCreated()
    {
        return created;
    }

    @Override
    public void setCreated(Date created)
    {
        this.created = created;
    }

    @Override
    public String getCreatedBy()
    {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    @Override
    public Date getModified()
    {
        return modified;
    }

    @Override
    public void setModified(Date modified)
    {
        this.modified = modified;
    }

    @Override
    public String getModifiedBy()
    {
        return modifiedBy;
    }

    @Override
    public void setModifiedBy(String modifiedBy)
    {
        this.modifiedBy = modifiedBy;
    }
}
