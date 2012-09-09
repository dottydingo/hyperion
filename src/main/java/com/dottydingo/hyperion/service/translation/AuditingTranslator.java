package com.dottydingo.hyperion.service.translation;

import com.dottydingo.hyperion.api.BaseAuditableApiObject;
import com.dottydingo.hyperion.service.model.BaseAuditablePersistentObject;

import java.util.Date;

/**
 * User: mark
 * Date: 9/8/12
 * Time: 4:02 PM
 */
public abstract class AuditingTranslator<C extends BaseAuditableApiObject,P extends BaseAuditablePersistentObject>
    extends BaseTranslator<C,P>
{
    @Override
    protected void afterConvert(C client, P persistent, TranslationContext context)
    {
        super.afterConvert(client, persistent, context);
        Date now = new Date();
        persistent.setCreated(now);
        persistent.setCreatedBy(context.getUserString());
        persistent.setModified(now);
        persistent.setModifiedBy(context.getUserString());
    }

    @Override
    protected void beforeCopy(C client, P persistent, TranslationContext context)
    {
        super.beforeCopy(client, persistent, context);
        client.setCreated(null);
        client.setCreatedBy(null);
        client.setModified(null);
        client.setModifiedBy(null);
    }

    @Override
    protected void afterCopy(C client, P persistent, TranslationContext context)
    {
        super.afterCopy(client, persistent, context);
        persistent.setModified(new Date());
        persistent.setModifiedBy(context.getUserString()) ;
    }
}
