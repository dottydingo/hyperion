package com.dottydingo.hyperion.service.context;

import com.dottydingo.service.endpoint.context.AbstractContextBuilder;

import javax.servlet.http.HttpServletRequest;


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
}
