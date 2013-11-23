package com.dottydingo.hyperion.service.context;

import com.dottydingo.hyperion.service.configuration.HyperionEndpointConfiguration;
import com.dottydingo.service.endpoint.context.AbstractContextBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 */
public class HyperionContextBuilder extends AbstractContextBuilder<HyperionContext,HyperionRequest,HyperionResponse>
{

    @Override
    protected void setupRequest(HttpServletRequest httpServletRequest, HyperionRequest request)
    {
        super.setupRequest(httpServletRequest,request);
        request.setResourceUri(getResourceUri(httpServletRequest));
    }

    @Override
    protected void setupResponse(HttpServletResponse httpServletResponse, HyperionResponse response)
    {
        super.setupResponse(httpServletResponse, response);
        response.setHeader("Access-Control-Allow-Origin",
                ((HyperionEndpointConfiguration)endpointConfiguration).getAllowedOrigins());
    }

    @Override
    public HyperionContext buildContext(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException
    {
        HyperionContext hyperionContext = super.buildContext(httpServletRequest, httpServletResponse);
        hyperionContext.setShowErrorDetail(shouldIncludeErrorDetail(hyperionContext.getEndpointRequest()));
        return hyperionContext;
    }

    @Override
    protected HyperionContext createContextInstance()
    {
        return new HyperionContext();
    }

    @Override
    protected HyperionResponse createResponseInstance()
    {
        return new HyperionResponse();
    }

    @Override
    protected HyperionRequest createRequestInstance()
    {
        return new HyperionRequest();
    }

    protected String getResourceUri(HttpServletRequest request)
    {
        String context = request.getContextPath();
        String servlet = request.getServletPath();
        String requestUri = request.getRequestURI();

        if((context.length() + servlet.length()) == requestUri.length())
            return "";

        return requestUri.substring(context.length() + servlet.length());
    }

    protected boolean shouldIncludeErrorDetail(HyperionRequest request)
    {
        boolean includeErrorDetail = ((HyperionEndpointConfiguration)endpointConfiguration).getIncludeErrorDetail();
        if(!includeErrorDetail)
            includeErrorDetail = request.getParameter("errorDetail") != null;
        return includeErrorDetail;
    }
}
