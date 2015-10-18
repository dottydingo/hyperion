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
import com.dottydingo.hyperion.core.endpoint.HyperionContext;
import com.dottydingo.hyperion.core.endpoint.status.ServiceStatus;
import com.dottydingo.service.endpoint.context.EndpointRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;


/**
 */
public class EndpointValidationPhase extends BaseHyperionPhase
{
    private static final String SERVICE_NOT_AVAILABLE = "ERROR_SERVICE_NOT_AVAILABLE";
    private static final String URI_NOT_RECOGNIZED = "ERROR_URI_NOT_RECOGNIZED";
    private static final String INVALID_ENTITY = "ERROR_INVALID_ENTITY";
    private static final String METHOD_NOT_ALLOWED = "ERROR_METHOD_NOT_ALLOWED";
    private static final String READ_ONLY_MODE = "ERROR_READ_ONLY_MODE";
    private static final String MISSING_VERSION_PARAMETER = "ERROR_MISSING_VERSION_PARAMETER";
    private static final String INVALID_VERSION = "ERROR_INVALID_VERSION";
    private static final String UNKNOWN_VERSION = "ERROR_UNKNOWN_VERSION";
    private static final String NOT_AUTHORIZED = "ERROR_NOT_AUTHORIZED";
    public static final String ENC = "UTF-8";

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
            throw new ServiceUnavailableException(messageSource.getErrorMessage(SERVICE_NOT_AVAILABLE,
                    phaseContext.getLocale()));

        HyperionRequest request = phaseContext.getEndpointRequest();
        HyperionResponse response = phaseContext.getEndpointResponse();

        UriRequestResult uriRequestResult = uriParser.parseRequestUri(request.getResourceUri());

        if(uriRequestResult == null)
            throw new NotFoundException(
                    messageSource.getErrorMessage(URI_NOT_RECOGNIZED,phaseContext.getLocale(),request.getResourceUri()));

        String entityName = uriRequestResult.getEndpoint();

        EntityPlugin plugin = serviceRegistry.getPluginForName(entityName);
        if(plugin == null)
            throw new NotFoundException(messageSource.getErrorMessage(INVALID_ENTITY, phaseContext.getLocale(),
                    entityName));

        phaseContext.setEntityPlugin(plugin);

        phaseContext.setRequestMethod(getHttpMethod(request.getRequestMethod(), phaseContext));
        String requestMethod = getEffectiveMethod(request);
        HttpMethod httpMethod = getHttpMethod(requestMethod, phaseContext);

        if(!plugin.isMethodAllowed(httpMethod))
            throw new NotAllowedException(messageSource.getErrorMessage(METHOD_NOT_ALLOWED, phaseContext.getLocale(),
                    httpMethod));

        if(serviceStatus.getReadOnly() && httpMethod.isWriteOperation())
            throw new NotAllowedException(messageSource.getErrorMessage(READ_ONLY_MODE,phaseContext.getLocale()));

        phaseContext.setEffectiveMethod(httpMethod);

        if(!uriRequestResult.isHistory())
        {
            // special case where version is in the URI
            String version = uriRequestResult.getVersion();

            if (version == null || version.length() == 0)
            {
                version = request.getFirstParameter(configuration.getVersionParameterName());
                if (version == null || version.length() == 0)
                    version = request.getFirstHeader(configuration.getVersionHeaderName());

                if (configuration.isRequireVersion() && httpMethod != HttpMethod.DELETE &&
                        (version == null || version.length() == 0))
                    throw new BadRequestException(
                            messageSource.getErrorMessage(MISSING_VERSION_PARAMETER, phaseContext.getLocale(),
                                    configuration.getVersionParameterName()));
            }


            if (version != null)
            {
                try
                {
                    phaseContext.setVersion(Integer.parseInt(version));
                }
                catch (NumberFormatException e)
                {
                    throw new BadRequestException(
                            messageSource.getErrorMessage(INVALID_VERSION, phaseContext.getLocale(),
                                    version));
                }
            }

            if (version != null && configuration.isRequireValidVersion() &&
                    !plugin.getApiVersionRegistry().isValid(phaseContext.getVersion()))
                throw new BadRequestException(
                        messageSource.getErrorMessage(UNKNOWN_VERSION, phaseContext.getLocale(),
                                version));
        }

        if(!validateMethod(httpMethod,uriRequestResult))
            throw new NotAllowedException(messageSource.getErrorMessage(METHOD_NOT_ALLOWED,phaseContext.getLocale(),httpMethod));

        if(uriRequestResult.getId() != null)
            phaseContext.setId(URLDecoder.decode(uriRequestResult.getId(), ENC));
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
            throw new AuthorizationException(messageSource.getErrorMessage(NOT_AUTHORIZED,phaseContext.getLocale()));

    }

    protected HttpMethod getHttpMethod(String methodName, HyperionContext context)
    {
        HttpMethod httpMethod;
        try
        {
            httpMethod = HttpMethod.valueOf(methodName);
        }
        catch (IllegalArgumentException e)
        {
            throw new NotAllowedException(messageSource.getErrorMessage(METHOD_NOT_ALLOWED,context.getLocale(),methodName));
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
            case PUT:
                return (requestResult.getId() == null && !requestResult.isHistory());
            case GET:
                return (requestResult.isHistory() && requestResult.getId() != null) || !requestResult.isHistory();
            case OPTIONS:
                return true;
            default:
                return false;
        }

    }
}
