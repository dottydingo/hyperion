package com.dottydingo.hyperion.core.translation;

import com.dottydingo.hyperion.api.AuditableApiObject;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.hyperion.core.model.AuditablePersistentObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A base field mapper for auditable entities
 */
public abstract class AuditingTranslator<C extends AuditableApiObject,P extends AuditablePersistentObject>
    extends BaseTranslator<C,P>
{

    @Override
    protected List<FieldMapper> getCustomFieldMappers()
    {
        List<FieldMapper> mappers = new ArrayList<FieldMapper>();
        mappers.addAll(super.getCustomFieldMappers());
        mappers.add(new ReadOnlyFieldMapper("created"));
        mappers.add(new ReadOnlyFieldMapper("createdBy"));
        mappers.add(new ReadOnlyFieldMapper("modified"));
        mappers.add(new ReadOnlyFieldMapper("modifiedBy"));
        return mappers;
    }

    @Override
    protected void afterConvert(ObjectWrapper<C> clientObjectWrapper, ObjectWrapper<P> persistentObjectWrapper, PersistenceContext context)
    {
        super.afterConvert(clientObjectWrapper, persistentObjectWrapper,context);
        P persistent = persistentObjectWrapper.getWrappedObject();
        persistent.setCreated(context.getCurrentTimestamp());
        persistent.setCreatedBy(context.getUserContext().getUserId());
        persistent.setModified(context.getCurrentTimestamp());
        persistent.setModifiedBy(context.getUserContext().getUserId());
    }


    @Override
    public boolean copyClient(C client, P persistent, PersistenceContext context)
    {
        boolean dirty = super.copyClient(client, persistent, context);

        if(dirty)
        {
            persistent.setModified(context.getCurrentTimestamp());
            persistent.setModifiedBy(context.getUserContext().getUserId()) ;
        }
        return dirty;
    }


}
