package com.dottydingo.hyperion.spring.boot;

import com.dottydingo.service.endpoint.EndpointHandler;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 */
public class HyperionEndpointServlet extends HttpServlet
{
    private EndpointHandler endpointHandler;

    public HyperionEndpointServlet(EndpointHandler endpointHandler)
    {
        this.endpointHandler = endpointHandler;
    }

    @Override
    public ServletConfig getServletConfig()
    {
        return null;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        endpointHandler.handleRequest(req, resp);
    }
}
