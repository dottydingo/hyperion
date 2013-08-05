package com.dottydingo.hyperion.service.context;

import com.dottydingo.service.endpoint.context.ContextBuilder;
import com.dottydingo.service.endpoint.context.UserContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 */
public class HyperionContextBuilder extends ContextBuilder<HyperionContext,HyperionRequest,HyperionResponse,UserContext>
{

    @Override
    protected HyperionRequest createRequest(HttpServletRequest httpServletRequest)
    {
        HyperionRequest request = super.createRequest(httpServletRequest);
        request.setResourceUri(getResourceUri(httpServletRequest));
        return request;
    }

    @Override
    protected HyperionContext createContextInstance(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
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
