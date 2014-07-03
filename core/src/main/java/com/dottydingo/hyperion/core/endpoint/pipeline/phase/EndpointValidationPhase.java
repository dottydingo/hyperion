package com.dottydingo.hyperion.core.endpoint.pipeline.phase;

import com.dottydingo.hyperion.api.exception.*;
import com.dottydingo.hyperion.core.registry.ApiVersionPlugin;
import com.dottydingo.hyperion.core.registry.EntityPlugin;
import com.dottydingo.hyperion.core.registry.ServiceRegistry;
import com.dottydingo.hyperion.core.endpoint.HyperionRequest;
import com.dottydingo.hyperion.core.endpoint.HyperionResponse;
import com.dottydingo.hyperion.core.endpoint.HttpMethod;
import com.dottydingo.hyperion.core.endpoint.pipeline.auth.AuthorizationContext;
import com.dottydingo.hyperion.core.endpoint.pipeline.auth.AuthorizationProvider;
import com.dottydingo.hyperion.core.configuration.HyperionEndpointConfiguration;
import com.dottydingo.hyperion.core.endpoint.HyperionContext;
import com.dottydingo.hyperion.core.endpoint.status.ServiceStatus;
import com.dottydingo.service.endpoint.context.EndpointRequest;
import com.dottydingo.service.endpoint.pipeline.AbstractEndpointPhase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;


/**
 */
public class EndpointValidationPhase extends BaseHyperionPhase
{
    private Logger logger = LoggerFactory.getLogger(EndpointValidationPhase.class);

    private ServiceRegistry serviceRegistry;
    private AuthorizationProvider authorizationProvider;
    private UriParser uriParser ;
    private ServiceStatus serviceStatus;

    public void setServiceRegistry(ServiceRegistry serviceRegistry)
    {
        this.serviceRegistry = serviceRegistry;
    }

    public void setAuthorizationProvider(AuthorizationProvider authorizationProvider)
    {
        this.authorizationProvider = authorizationProvider;
    }

    public void setUriParser(UriParser uriParser)
    {
        this.uriParser = uriParser;
    }

    public void setServiceStatus(ServiceStatus serviceStatus)
    {
        this.serviceStatus = serviceStatus;
    }

    @Override
    protected void executePhase(HyperionContext phaseContext) throws Exception
    {
        if(serviceStatus.getForceDown())
            throw new ServiceUnavailableException("Service not available");

        HyperionRequest request = phaseContext.getEndpointRequest();
        HyperionResponse response = phaseContext.getEndpointResponse();

        UriRequestResult uriRequestResult = uriParser.parseRequestUri(request.getResourceUri());

        if(uriRequestResult == null)
            throw new NotFoundException(String.format("%s is not recognized.",request.getResourceUri()));

        String entityName = uriRequestResult.getEndpoint();

        EntityPlugin plugin = serviceRegistry.getPluginForName(entityName);
        if(plugin == null)
            throw new NotFoundException(String.format("%s is not a valid entity.",entityName));

        phaseContext.setEntityPlugin(plugin);

        phaseContext.setRequestMethod(getHttpMethod(request.getRequestMethod()));
        String requestMethod = getEffectiveMethod(request);
        HttpMethod httpMethod = getHttpMethod(requestMethod);

        if(!plugin.isMethodAllowed(httpMethod))
            throw new NotAllowedException(String.format("%s is not allowed.",httpMethod));

        if(serviceStatus.getReadOnly() && httpMethod.isWriteOperation())
            throw new NotAllowedException("Service is in read only mode.");

        phaseContext.setEffectiveMethod(httpMethod);

        // special case where version is in the URI
        String version = uriRequestResult.getVersion();

        if(version == null || version.length() == 0)
        {
            version = request.getFirstParameter(configuration.getVersionParameterName());
            if(version == null || version.length() == 0)
                version = request.getFirstHeader(configuration.getVersionHeaderName());

            if(configuration.isRequireVersion() && httpMethod != HttpMethod.DELETE &&
                    (version == null || version.length()==0))
                throw new BadRequestException(String.format("The %s parameter must be specified",
                        configuration.getVersionParameterName()));
        }


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

        if(!validateMethod(httpMethod,uriRequestResult))
            throw new HyperionException(405,"Not allowed.");

        if(uriRequestResult.getId() != null)
            phaseContext.setId(URLDecoder.decode(uriRequestResult.getId(),"UFT-8"));
        phaseContext.setHistory(uriRequestResult.isHistory());

        ApiVersionPlugin versionPlugin = plugin.getApiVersionRegistry().getPluginForVersion(phaseContext.getVersion());
        phaseContext.setVersionPlugin(versionPlugin);

        logRequestInformation(phaseContext);

        if(phaseContext.getEffectiveMethod() == HttpMethod.GET)
        {
            response.setCacheMaxAge(plugin.getCacheMaxAge());
        }

        AuthorizationContext authorizationContext = authorizationProvider.authorize(phaseContext);
        phaseContext.setAuthorizationContext(authorizationContext);

        if(!authorizationContext.isAuthorized())
            throw new AuthorizationException("Not Authorized");

    }

    protected HttpMethod getHttpMethod(String methodName)
    {
        HttpMethod httpMethod;
        try
        {
            httpMethod = HttpMethod.valueOf(methodName);
        }
        catch (IllegalArgumentException e)
        {
            throw new HyperionException(405,String.format("%s is not allowed.", methodName));
        }
        return httpMethod;
    }

    protected void logRequestInformation(HyperionContext context)
    {
        if(!logger.isDebugEnabled())
            return;

        HyperionRequest request = context.getEndpointRequest();

        logger.debug("Correlation ID: {}",context.getCorrelationId());
        logger.debug("Request URL: {}",request.getRequestUrl());
        logger.debug("Base URL: {}",request.getBaseUrl());
        logger.debug("Request URI: {}",request.getRequestUri());
        logger.debug("Resource URI: {}",request.getResourceUri());
        logger.debug("Request Query String: {}",request.getQueryString());
        logger.debug("Request Method: {}",request.getRequestMethod());
        logger.debug("Effective Method: {}",context.getEffectiveMethod());
        logger.debug("Endpoint Name: {}",context.getEntityPlugin().getEndpointName());
        logger.debug("ContentType: {}",request.getContentType());
        logger.debug("User ID: {}",context.getUserContext().getUserId());
        logger.debug("User Name: {}",context.getUserContext().getUserName());
        logger.debug("Id: {}",context.getId());
        logger.debug("History: {}",context.isHistory());

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

    protected String getEffectiveMethod(EndpointRequest request)
    {
        if(request.getRequestMethod().equalsIgnoreCase("POST") && request.getContentType() != null &&
                request.getContentType().contains("application/x-www-form-urlencoded"))
        {
            return "GET";
        }
        else if(request.getRequestMethod().equalsIgnoreCase("HEAD"))
            return "GET";

        return request.getRequestMethod();
    }

    protected boolean validateMethod(HttpMethod method,UriRequestResult requestResult)
    {
        switch (method)
        {
            case DELETE:
                return (requestResult.getId() != null && !requestResult.isHistory());
            case POST:
                return (requestResult.getId() == null && !requestResult.isHistory());
            case PUT:
                return (!requestResult.isHistory());
            case GET:
                return (requestResult.isHistory() && requestResult.getId() != null) || !requestResult.isHistory();
            case OPTIONS:
                return true;
            default:
                return false;
        }

    }
}
