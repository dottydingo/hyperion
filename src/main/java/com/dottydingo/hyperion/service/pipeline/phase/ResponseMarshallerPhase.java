package com.dottydingo.hyperion.service.pipeline.phase;

import com.dottydingo.hyperion.service.endpoint.HttpMethod;
import com.dottydingo.hyperion.service.marshall.EndpointMarshaller;
import com.dottydingo.hyperion.service.configuration.HyperionEndpointConfiguration;
import com.dottydingo.hyperion.service.context.HyperionContext;
import com.dottydingo.service.endpoint.CompletionHandler;
import com.dottydingo.service.endpoint.context.EndpointResponse;
import com.dottydingo.service.endpoint.pipeline.AbstractEndpointPhase;

/**
 */
public class ResponseMarshallerPhase extends AbstractEndpointPhase<HyperionContext>
{
    private EndpointMarshaller marshaller;
    private CompletionHandler completionHandler;
    private HyperionEndpointConfiguration configuration;

    public void setMarshaller(EndpointMarshaller marshaller)
    {
        this.marshaller = marshaller;
    }

    public void setCompletionHandler(CompletionHandler completionHandler)
    {
        this.completionHandler = completionHandler;
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

            if(phaseContext.getHttpMethod() != HttpMethod.DELETE)
                response.setHeader(configuration.getVersionHeaderName(),phaseContext.getVersionPlugin().getVersion().toString());

            marshaller.marshall(phaseContext.getEndpointResponse().getOutputStream(),result);
        }

        completionHandler.completeRequest(phaseContext);
    }
}
