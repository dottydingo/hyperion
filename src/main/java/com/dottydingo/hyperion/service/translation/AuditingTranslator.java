package com.dottydingo.hyperion.service.translation;

import com.dottydingo.hyperion.api.BaseAuditableApiObject;
import com.dottydingo.hyperion.service.context.RequestContext;
import com.dottydingo.hyperion.service.model.AuditablePersistentObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A base field mapper for auditable entities
 */
public abstract class AuditingTranslator<C extends BaseAuditableApiObject,P extends AuditablePersistentObject>
    extends BaseTranslator<C,P>
{

    @Override
    protected List<FieldMapper> getCustomFieldMappers()
    {
        List<FieldMapper> mappers = new ArrayList<FieldMapper>();
        mappers.add(new ReadOnlyFieldMapper("created",clientBeanMap,persistentBeanMap));
        mappers.add(new ReadOnlyFieldMapper("createdBy",clientBeanMap,persistentBeanMap));
        mappers.add(new ReadOnlyFieldMapper("modified",clientBeanMap,persistentBeanMap));
        mappers.add(new ReadOnlyFieldMapper("modifiedBy",clientBeanMap,persistentBeanMap));
        return mappers;
    }

    @Override
    protected void afterConvert(C client, P persistent, RequestContext context)
    {
        super.afterConvert(client, persistent, context);
        Date now = new Date();
        persistent.setCreated(now);
        persistent.setCreatedBy(context.getAuthorizationContext().getUserString());
        persistent.setModified(now);
        persistent.setModifiedBy(context.getAuthorizationContext().getUserString());
    }

    @Override
    protected void beforeCopy(C client, P persistent, RequestContext context)
    {
        super.beforeCopy(client, persistent, context);
    }

    @Override
    protected void afterCopy(C client, P persistent, RequestContext context)
    {
        super.afterCopy(client, persistent, context);
        persistent.setModified(new Date());
        persistent.setModifiedBy(context.getAuthorizationContext().getUserString()) ;
    }
}
