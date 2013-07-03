package com.dottydingo.hyperion.service.pipeline;

import com.dottydingo.hyperion.exception.BadRequestException;
import com.dottydingo.hyperion.exception.HyperionException;
import com.dottydingo.hyperion.exception.NotFoundException;
import com.dottydingo.hyperion.service.configuration.EntityPlugin;
import com.dottydingo.hyperion.service.configuration.ServiceRegistry;
import com.dottydingo.hyperion.service.endpoint.HttpMethod;
import com.dottydingo.hyperion.service.pipeline.configuration.HyperionEndpointConfiguration;
import com.dottydingo.hyperion.service.pipeline.context.HyperionContext;
import com.dottydingo.service.endpoint.context.EndpointRequest;
import com.dottydingo.service.pipeline.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class EndpointValidationPhase implements Phase<HyperionContext>
{
    private Logger logger = LoggerFactory.getLogger(EndpointValidationPhase.class);

    private ServiceRegistry serviceRegistry;
    private HyperionEndpointConfiguration hyperionEndpointConfiguration;

    public void setServiceRegistry(ServiceRegistry serviceRegistry)
    {
        this.serviceRegistry = serviceRegistry;
    }

    public void setHyperionEndpointConfiguration(HyperionEndpointConfiguration hyperionEndpointConfiguration)
    {
        this.hyperionEndpointConfiguration = hyperionEndpointConfiguration;
    }

    @Override
    public void execute(HyperionContext phaseContext) throws Exception
    {
        logger.debug("Starting EndpointValidationPhase");
        EndpointRequest request = phaseContext.getEndpointRequest();

        String[] split = request.getRequestUri().split("/");
        if(split.length < 2)
            throw new BadRequestException("Missing entity name.");

        String entityName = split[1];

        EntityPlugin plugin = serviceRegistry.getPluginForName(entityName);
        if(plugin == null)
            throw new NotFoundException(String.format("%s is not a valid entity.",entityName));

        phaseContext.setEntityPlugin(plugin);

        HttpMethod httpMethod = null;
        try
        {
            httpMethod = HttpMethod.valueOf(request.getRequestMethod());
        }
        catch (IllegalArgumentException e)
        {
            throw new HyperionException(405,String.format("%s is not allowed.",request.getRequestMethod()));
        }

        if(!plugin.isMethodAllowed(httpMethod))
            throw new HyperionException(405,String.format("%s is not allowed.",httpMethod));

        phaseContext.setHttpMethod(httpMethod);

        String version = request.getFirstParameter(hyperionEndpointConfiguration.getVersionParameterName());
        if(version == null || version.length() == 0)
            version = request.getFirstHeader(hyperionEndpointConfiguration.getVersionHeaderName());

        if(hyperionEndpointConfiguration.isRequireVersion() && httpMethod != HttpMethod.DELETE &&
                (version == null || version.length()==0))
            throw new BadRequestException(String.format("The %s parameter must be specified",hyperionEndpointConfiguration.getVersionParameterName()));

        if(version != null)
        {
            try
            {
                phaseContext.setVersion(Integer.parseInt(version));
            }
            catch(NumberFormatException e)
            {
                throw new BadRequestException(String.format("%s is not a valid value for version.",version));
            }
        }

        logRequestInformation(phaseContext);

        logger.debug("Ending EndpointValidationPhase");
    }

    protected void logRequestInformation(HyperionContext context)
    {
        if(!logger.isDebugEnabled())
            return;

        EndpointRequest request = context.getEndpointRequest();

        logger.debug("Request URL: {}",request.getRequestUrl());
        logger.debug("Base URL: {}",request.getBaseUrl());
        logger.debug("Request URI: {}",request.getRequestUri());
        logger.debug("Request Query String: {}",request.getQueryString());
        logger.debug("Request Method: {}",request.getRequestMethod());
        logger.debug("Effective Method: {}",context.getHttpMethod());
        logger.debug("Endpoint Name: {}",context.getEntityPlugin().getEndpointName());
        logger.debug("ContentType: {}",request.getContentType());

        for (String name : request.getHeaderNames())
        {
            logger.debug("Header name: {} value:{}",name,request.getHeader(name));
        }

        for (String name : request.getParameterNames())
        {
            logger.debug("Parameter name: {} value:{}",name,request.getParameter(name));
        }

        logger.debug("Request Version: {}",context.getVersion());
    }
}
