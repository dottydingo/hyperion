package com.dottydingo.hyperion.service.pipeline.phase;

import com.dottydingo.hyperion.exception.HyperionException;
import com.dottydingo.hyperion.service.context.HyperionContext;

/**
 */
public class AuditPhase extends BasePersistencePhase<HyperionContext>
{
    @Override
    protected void executePhase(HyperionContext phaseContext) throws Exception
    {
        throw new HyperionException(501,"Not implemented");
    }
}
