package com.dottydingo.hyperion.core.endpoint.pipeline.phase;

import com.dottydingo.hyperion.core.configuration.HyperionEndpointConfiguration;
import com.dottydingo.hyperion.core.endpoint.HyperionContext;
import com.dottydingo.hyperion.core.message.HyperionMessageSource;
import com.dottydingo.service.endpoint.pipeline.AbstractEndpointPhase;

/**
 */
public abstract class BaseHyperionPhase extends AbstractEndpointPhase<HyperionContext>
{
    protected HyperionMessageSource messageSource;
    protected HyperionEndpointConfiguration configuration;

    public void setMessageSource(HyperionMessageSource messageSource)
    {
        this.messageSource = messageSource;
    }

    public void setConfiguration(HyperionEndpointConfiguration configuration)
    {
        this.configuration = configuration;
    }
}
