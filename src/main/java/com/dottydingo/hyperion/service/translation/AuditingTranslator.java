package com.dottydingo.hyperion.service.translation;

import com.dottydingo.hyperion.api.AuditableApiObject;
import com.dottydingo.hyperion.service.persistence.PersistenceContext;
import com.dottydingo.hyperion.service.model.AuditablePersistentObject;

import java.util.ArrayList;
import java.util.Date;
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
    protected void afterCopy(ObjectWrapper<C> clientObjectWrapper, ObjectWrapper<P> persistentObjectWrapper, PersistenceContext context)
    {
        super.afterCopy(clientObjectWrapper, persistentObjectWrapper, context);

        if(context.isDirty())
        {
            P persistent = persistentObjectWrapper.getWrappedObject();
            persistent.setModified(context.getCurrentTimestamp());
            persistent.setModifiedBy(context.getUserContext().getUserId()) ;
        }
    }

}
