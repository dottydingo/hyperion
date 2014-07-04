package com.dottydingo.hyperion.core.endpoint.pipeline.phase;

import com.dottydingo.hyperion.api.exception.InternalException;
import com.dottydingo.hyperion.core.endpoint.HttpMethod;
import com.dottydingo.hyperion.core.endpoint.marshall.EndpointMarshaller;
import com.dottydingo.hyperion.core.configuration.HyperionEndpointConfiguration;
import com.dottydingo.hyperion.core.endpoint.HyperionContext;
import com.dottydingo.hyperion.core.endpoint.marshall.MarshallingException;
import com.dottydingo.service.endpoint.context.EndpointResponse;
import com.dottydingo.service.endpoint.pipeline.AbstractEndpointPhase;

/**
 */
public class ResponseMarshallerPhase extends BaseHyperionPhase
{
    private EndpointMarshaller marshaller;

    public void setMarshaller(EndpointMarshaller marshaller)
    {
        this.marshaller = marshaller;
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
            catch(MarshallingException e)
            {
                logger.warn("Error marshalling response.",e);
            }

        }

        phaseContext.requestComplete();
    }
}
