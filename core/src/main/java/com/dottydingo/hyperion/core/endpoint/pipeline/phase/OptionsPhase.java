package com.dottydingo.hyperion.core.endpoint.pipeline.phase;

import com.dottydingo.hyperion.core.configuration.HyperionEndpointConfiguration;
import com.dottydingo.hyperion.core.endpoint.HyperionContext;
import com.dottydingo.hyperion.core.endpoint.HyperionRequest;
import com.dottydingo.hyperion.core.endpoint.HyperionResponse;
import com.dottydingo.hyperion.core.endpoint.HttpMethod;
import com.dottydingo.service.endpoint.pipeline.AbstractEndpointPhase;

import java.util.*;

/**
 */
public class OptionsPhase extends BaseHyperionPhase
{
    @Override
    protected void executePhase(HyperionContext phaseContext) throws Exception
    {
        HyperionRequest request = phaseContext.getEndpointRequest();
        HyperionResponse response = phaseContext.getEndpointResponse();

        Set<HttpMethod> allowedMethods = getAllowedMethods(phaseContext);

        StringBuilder sb = new StringBuilder();
        int ct = 0;
        for (HttpMethod allowedMethod : allowedMethods)
        {
            if(ct++ > 0)
                sb.append(", ");
            sb.append(allowedMethod.toString());
        }
        response.setHeader("Access-Control-Allow-Methods",sb.toString());

        String requestedHeaders =  request.getFirstHeader("Access-Control-Request-Headers");
        if(requestedHeaders != null && requestedHeaders.length() > 0)
            response.setHeader("Access-Control-Allow-Headers",requestedHeaders);

        //response.setHeader("Access-Control-Expose-Headers",""); todo
        //response.setHeader("Control-Allow-Credentials",""); todo

        if(configuration.getAccessControlMaxAge() > 0)
            response.setHeader("Access-Control-Max-Age", Integer.toString(configuration.getAccessControlMaxAge()));

        response.setContentType("application/json");
        response.setContentEncoding("UTF-8");
        response.setResponseCode(200);

        phaseContext.requestComplete();

    }

    protected Set<HttpMethod> getAllowedMethods(HyperionContext context)
    {
        Set<HttpMethod> methods = new HashSet<HttpMethod>();
        if(context.getId() != null)
        {
            methods.add(HttpMethod.GET);
            methods.add(HttpMethod.HEAD);
            if(!context.isHistory())
            {
                methods.add(HttpMethod.PUT);
                methods.add(HttpMethod.DELETE);
            }
        }
        else
        {
            methods.add(HttpMethod.GET);
            methods.add(HttpMethod.POST);
            methods.add(HttpMethod.PUT);
            methods.add(HttpMethod.HEAD);
        }

        context.getEntityPlugin().filterAllowedMethods(methods);
        return methods;
    }

}
