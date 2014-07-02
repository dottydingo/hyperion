package com.dottydingo.hyperion.core.endpoint;

import com.dottydingo.hyperion.core.configuration.HyperionEndpointConfiguration;
import com.dottydingo.service.endpoint.context.AbstractContextBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 */
public class HyperionContextBuilder extends AbstractContextBuilder<HyperionContext,HyperionRequest,HyperionResponse>
{

    @Override
    protected void setupRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, HyperionRequest request)
            throws IOException
    {
        super.setupRequest(httpServletRequest, httpServletResponse, request);
        request.setResourceUri(getResourceUri(httpServletRequest));
    }

    @Override
    protected void setupResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, HyperionResponse response)
            throws IOException
    {
        super.setupResponse(httpServletRequest, httpServletResponse, response);
        response.setHeader("Access-Control-Allow-Origin",
                ((HyperionEndpointConfiguration)endpointConfiguration).getAllowedOrigins());
    }

    @Override
    public HyperionContext buildContext(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException
    {
        HyperionContext hyperionContext = super.buildContext(httpServletRequest, httpServletResponse);
        hyperionContext.setShowErrorDetail(shouldIncludeErrorDetail(hyperionContext.getEndpointRequest()));
        hyperionContext.setLocal(httpServletRequest.getLocale());
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
