package com.dottydingo.hyperion.swagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 */
public class SwaggerRequestHandler  implements HttpRequestHandler
{
    private SwaggerSpecBuilder swaggerSpecBuilder;
    private ObjectMapper objectMapper;

    public void setSwaggerSpecBuilder(SwaggerSpecBuilder swaggerSpecBuilder)
    {
        this.swaggerSpecBuilder = swaggerSpecBuilder;
    }

    public void setObjectMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws ServletException, IOException
    {
        String path = httpServletRequest.getPathInfo();
        if(path == null || path.length() == 0)
        {
            httpServletResponse.sendError(404);
            return;
        }

        if(path.equals("/"))
        {
            ResourceListing listing = swaggerSpecBuilder.buildResourceListing();
            objectMapper.writeValue(httpServletResponse.getOutputStream(),listing);
            return;
        }

        String[] split = path.split("/");
        if(split.length == 2)
        {
            ApiDeclaration declaration = swaggerSpecBuilder.buildEndpoint(split[1]);
            if(declaration == null)
            {
                httpServletResponse.sendError(404);
                return;
            }
            objectMapper.writeValue(httpServletResponse.getOutputStream(),declaration);
        }
        else
        {
            httpServletResponse.sendError(404);
        }
    }
}
