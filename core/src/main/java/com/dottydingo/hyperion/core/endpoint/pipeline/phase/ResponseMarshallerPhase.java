package com.dottydingo.hyperion.core.endpoint.pipeline.phase;

import com.dottydingo.hyperion.api.exception.InternalException;
import com.dottydingo.hyperion.core.endpoint.HttpMethod;
import com.dottydingo.hyperion.core.endpoint.marshall.EndpointMarshaller;
import com.dottydingo.hyperion.core.configuration.HyperionEndpointConfiguration;
import com.dottydingo.hyperion.core.endpoint.HyperionContext;
import com.dottydingo.service.endpoint.context.EndpointResponse;
import com.dottydingo.service.endpoint.pipeline.AbstractEndpointPhase;

/**
 */
public class ResponseMarshallerPhase extends AbstractEndpointPhase<HyperionContext>
{
    private EndpointMarshaller marshaller;
    private HyperionEndpointConfiguration configuration;

    public void setMarshaller(EndpointMarshaller marshaller)
    {
        this.marshaller = marshaller;
    }

    public void setConfiguration(HyperionEndpointConfiguration configuration)
    {
        this.configuration = configuration;
    }

    @Override
    protected void executePhase(HyperionContext phaseContext) throws Exception
    {
        Object result = phaseContext.getResult();
        if(result != null)
        {
            EndpointResponse response = phaseContext.getEndpointResponse();
            response.setContentType("application/json");
            response.setContentEncoding("UTF-8");

            if(phaseContext.getEffectiveMethod() != HttpMethod.DELETE)
                response.setHeader(configuration.getVersionHeaderName(),phaseContext.getVersionPlugin().getVersion().toString());

            try
            {
                if(phaseContext.getRequestMethod() != HttpMethod.HEAD)
                    marshaller.marshall(phaseContext.getEndpointResponse().getOutputStream(),result);
            }
            catch(InternalException e)
            {
                logger.error("Error marshalling response.",e);
            }

        }

        phaseContext.requestComplete();
    }
}
