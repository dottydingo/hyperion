package com.dottydingo.hyperion.client;

import com.dottydingo.hyperion.api.*;
import com.dottydingo.hyperion.api.exception.HyperionException;
import com.dottydingo.hyperion.client.event.ClientEvent;
import com.dottydingo.hyperion.client.event.ClientEventListener;
import com.dottydingo.hyperion.client.exception.ClientConnectionException;
import com.dottydingo.hyperion.client.exception.ClientException;
import com.dottydingo.hyperion.client.exception.ClientMarshallingException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;
import okio.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;

/**
 * Client for making requests to Hyperion 2.x services. The client is thread safe and a single instance should generally
 * be used to access all endpoints on a service.
 */
public class HyperionClient
{
    private static final long DEFAULT_MAX_LOGGED_BODY_SIZE = 1024 * 1000;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapperBuilder().getObjectMapper();

    private static final String CLIENT_VERSION_HEADER_NAME = "DottyDingo-Hyperion-Client-Version";
    private static final String CLIENT_VERSION = "2";

    protected Logger logger = LoggerFactory.getLogger(HyperionClient.class);
    protected String baseUrl;
    protected OkHttpClient client;
    protected ObjectMapper objectMapper = DEFAULT_OBJECT_MAPPER;
    protected ParameterFactory parameterFactory;
    protected HeaderFactory headerFactory;
    protected AuthorizationFactory authorizationFactory;
    protected ClientEventListener clientEventListener;
    protected String userAgent = "hyperionClient";
    protected long maxLoggedBodySize = DEFAULT_MAX_LOGGED_BODY_SIZE;

    /**
     * Create a client with the supplied parameters.
     * @param baseUrl The base URL for the service
     */
    public HyperionClient(String baseUrl)
    {
        this(baseUrl,false);
    }

    /**
     * Create a client with the supplied parameters.
     * @param baseUrl The base URL for the service
     * @param trustAllCertificates A flag indicating if all certificates should be trusted. This should only
     *                             be set to true when calling a service using a self signed certificate.
     */
    public HyperionClient(String baseUrl, boolean trustAllCertificates)
    {
        this.baseUrl = baseUrl;
        if (!baseUrl.endsWith("/"))
            this.baseUrl+="/";

        client = buildClient(trustAllCertificates);
    }

    /**
     * Create a client with the supplied parameters.
     * @param baseUrl The base URL for the service
     * @param authorizationFactory The authorization factory to use for making requests
     */
    public HyperionClient(String baseUrl, AuthorizationFactory authorizationFactory)
    {
        this(baseUrl,authorizationFactory,false);
    }

    /**
     * Create a client with the supplied parameters.
     * @param baseUrl The base URL for the service
     * @param authorizationFactory The authorization factory to use for making requests
     * @param trustAllCertificates A flag indicating if all certificates should be trusted. This should only
     *                             be set to true when calling a service using a self signed certificate.
     */
    public HyperionClient(String baseUrl, AuthorizationFactory authorizationFactory,boolean trustAllCertificates)
    {
        this(baseUrl,trustAllCertificates);
        this.authorizationFactory = authorizationFactory;
    }

    /**
     * Set a parameter factory to use.
     * @param parameterFactory The parameter factory
     */
    public void setParameterFactory(ParameterFactory parameterFactory)
    {
        this.parameterFactory = parameterFactory;
    }

    /**
     * Set a header factory to use.
     * @param headerFactory The parameter factory
     */
    public void setHeaderFactory(HeaderFactory headerFactory)
    {
        this.headerFactory = headerFactory;
    }

    /**
     * Set an alternate object mapper to use for marshalling and unmarshalling JSON.
     * @param objectMapper An object mapper.
     */
    public void setObjectMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    /**
     * Set the proxy configuration to use for this client.
     * @param proxy The proxy configuration
     */
    public void setProxy(Proxy proxy)
    {
        if(proxy != null)
        {
            java.net.Proxy p = new java.net.Proxy(java.net.Proxy.Type.HTTP,
                    new InetSocketAddress(proxy.getHost(), proxy.getPort()));
            client.setProxy(p);
        }
        else
            client.setProxy(java.net.Proxy.NO_PROXY);
    }

    /**
     * Set the keep alive configuration to use for this client.
     * @param keepAliveConfiguration The keep alive configuration
     */
    public void setKeepAliveConfiguration(KeepAliveConfiguration keepAliveConfiguration)
    {
        if(keepAliveConfiguration != null)
            client.setConnectionPool(new ConnectionPool(keepAliveConfiguration.getMaxIdleConnections(),
                    keepAliveConfiguration.getKeepAliveDurationMs()));
    }

    /**
     * Set the client event listener
     * @param clientEventListener The client event listener
     */
    public void setClientEventListener(ClientEventListener clientEventListener)
    {
        this.clientEventListener = clientEventListener;
    }

    /**
     * Set the user agent string to send on the requests. The default value is "hyperionClient"
     * @param userAgent The user agent string
     */
    public void setUserAgent(String userAgent)
    {
        this.userAgent = userAgent;
    }

    /**
     * Set an alternate logger for this client to use for log statements. This can be used to direct client
     * logs to different destinations. The default value is "com.dottydingo.hyperion.client.HyperionClient"
     * @param logger The logger to use
     */
    public void setLogger(String logger)
    {
        if(logger != null && logger.trim().length() > 0)
            this.logger = LoggerFactory.getLogger(logger);
    }

    /**
     * Set the maximum size for logging the request and response bodies. When logging is set to DEBUG these will be
     * logged as long as the contents are less than or equal to this value. The default value is 1 megabyte
     * @param maxLoggedBodySize The maximum size of a body to log, in bytes
     */
    public void setMaxLoggedBodySize(long maxLoggedBodySize)
    {
        this.maxLoggedBodySize = maxLoggedBodySize;
    }

    /**
     * Perform a get (GET) operation using the supplied request.
     * @param request The request
     * @return The results of the get operation
     */
    public <T extends ApiObject> EntityList<T> get(Request<T> request)
    {
        return executeRequest(request,objectMapper.getTypeFactory()
                .constructParametrizedType(EntityList.class,EntityList.class, request.getEntityType()));
    }

    /**
     * Perform a query (GET) operation using the supplied request
     * @param request The request
     * @return The results of the query operation
     */
    public <T extends ApiObject> EntityResponse<T> query(Request<T> request)
    {
        return executeRequest(request, objectMapper.getTypeFactory()
                .constructParametrizedType(EntityResponse.class, EntityResponse.class, request.getEntityType()));
    }

    /**
     * Perform a delete (DELETE) operation using the supplied request
     * @param request The request
     * @return The number of items deleted
     */
    public int delete(Request request)
    {
        DeleteResponse response = executeRequest(request,objectMapper.getTypeFactory().constructType(
                DeleteResponse.class));
        return response.getCount();
    }

    /**
     * Perform a create (POST) operation using the supplied request
     * @param request The request
     * @return The results of the create operation
     */
    public <T extends ApiObject> EntityList<T> create(Request<T> request)
    {
        return executeRequest(request,objectMapper.getTypeFactory()
                .constructParametrizedType(EntityList.class,EntityList.class, request.getEntityType()));
    }

    /**
     * Perform an update (PUT) operation using the supplied request
     * @param request The request
     * @return The results of the update operation
     */
    public <T extends ApiObject> EntityList<T> update(Request<T> request)
    {
        return executeRequest(request,objectMapper.getTypeFactory()
                .constructParametrizedType(EntityList.class,EntityList.class, request.getEntityType()));
    }

    /**
     * Execute the request
     * @param request The request
     * @param javaType The return type
     * @return The response
     */
    protected <R> R executeRequest(Request request, JavaType javaType)
    {
        long start = System.currentTimeMillis();
        boolean error = true;
        try
        {
            Response response = executeRequest(request);
            R r = readResponse(response, javaType);
            error = false;
            return r;
        }
        finally
        {
            if(clientEventListener != null)
            {
                ClientEvent event = new ClientEvent(baseUrl,request.getEntityName(),request.getRequestMethod(),
                        System.currentTimeMillis() - start,error);
                clientEventListener.handleEvent(event);
            }
        }
    }

    /**
     * Build the actual http request
     * @param request The data service request
     * @return The http request
     */
    protected com.squareup.okhttp.Request buildHttpRequest(Request request)
    {
        RequestBody requestBody = null;
        if(request.getRequestMethod().isBodyRequest())
            requestBody = RequestBody.create(JSON, serializeBody(request));

        return new com.squareup.okhttp.Request.Builder()
                .url(buildUrl(request))
                .headers(getHeaders(request))
                .method(request.getRequestMethod().name(),requestBody)
                .build();
    }


    /**
     * Execute the request
     * @param request The data service request
     * @return The HTTP response
     */
    protected Response executeRequest(Request request)
    {
        try
        {
            com.squareup.okhttp.Request httpRequest = buildHttpRequest(request);

            if(logger.isInfoEnabled())
                logger.info("Sending request: {} {}",httpRequest.method(),httpRequest.urlString());

            if(logger.isDebugEnabled() && request.getRequestMethod().isBodyRequest())
            {
                Buffer buffer = new Buffer();
                httpRequest.body().writeTo(buffer);
                if(maxLoggedBodySize == -1 || buffer.size() <= maxLoggedBodySize )
                    logger.debug("Request body: {}", buffer.readUtf8());
                else
                    logger.debug("Request body not captured: too large. ");
            }

            if(logger.isTraceEnabled())
                logger.trace("Request headers: {}",httpRequest.headers().toString());

            Response response = client.newCall(httpRequest).execute();

            if(response.code() == HttpURLConnection.HTTP_UNAUTHORIZED && authorizationFactory != null
                    && authorizationFactory.retryOnAuthenticationError())
            {
                if(authorizationFactory instanceof ResettableAuthorizationFactory)
                    ((ResettableAuthorizationFactory) authorizationFactory).reset();

                response = client.newCall(httpRequest).execute();
            }

            logger.info("Response code: {}",response.code());

            if(logger.isTraceEnabled())
                logger.trace("Response headers: {}",response.headers().toString());

            if(logger.isDebugEnabled())
            {
                ByteArrayOutputStream copy = new ByteArrayOutputStream();
                copy(response.body().byteStream(),copy);
                response = response.newBuilder().body(ResponseBody.create(response.body().contentType(),copy.toByteArray())).build();

                if(maxLoggedBodySize == -1 || copy.size() <= maxLoggedBodySize)
                    logger.debug("Response body: {}",copy.toString());
                else
                    logger.debug("Response body not captured: too large.");
            }
            if (response.code() >= HttpURLConnection.HTTP_BAD_REQUEST)
            {
                throw readException(response);
            }

            return response;
        }
        catch (IOException e)
        {
            throw new ClientConnectionException("Error calling service.",e);
        }

    }


    /**
     * Read the response and unmarshall into the expected type
     * @param response The http response
     * @param javaType The type to return
     * @return The response value
     */
    protected <T> T readResponse(Response response,JavaType javaType)
    {
        try
        {
            return objectMapper.readValue(response.body().byteStream(), javaType);
        }
        catch (IOException e)
        {
            throw new ClientMarshallingException("Error reading results.",e);
        }
    }

    /**
     * Serialize the request body
     * @param request The data service request
     * @return The JSON representation of the request
     */
    protected String serializeBody(Request request)
    {
        try
        {
            return objectMapper.writeValueAsString(request.getRequestBody());
        }
        catch (JsonProcessingException e)
        {
            throw new ClientMarshallingException("Error writing request.",e);
        }
    }


    /**
     * Create the proper exception for the error response
     * @param response The http response
     * @return The exception for the error response
     * @throws IOException
     */
    protected HyperionException readException(Response response) throws IOException
    {
        ErrorResponse errorResponse = null;
        try
        {
            errorResponse = objectMapper.readValue(response.body().byteStream(), ErrorResponse.class);
        }
        catch (Exception ignore)
        {
        }

        HyperionException resolvedException = null;
        if (errorResponse != null)
        {
            try
            {
                Class exceptionClass = Class.forName(errorResponse.getType());
                resolvedException = (HyperionException) exceptionClass.getConstructor(String.class)
                        .newInstance(errorResponse.getMessage());
            }
            catch (Throwable ignore)
            {
            }

            if (resolvedException == null)
            {
                resolvedException =
                        new HyperionException(errorResponse.getStatusCode(), errorResponse.getMessage());
            }

            resolvedException.setErrorDetails(errorResponse.getErrorDetails());
            resolvedException.setErrorTime(errorResponse.getErrorTime());
            resolvedException.setRequestId(errorResponse.getRequestId());
        }

        if (resolvedException == null)
        {
            resolvedException =
                    new HyperionException(response.code(), response.message());
        }

        return resolvedException;

    }

    /**
     * Check to see if the supplied map has entries
     * @param map The map
     * @return True if the map has entries, false if the map is null or empty
     */
    protected boolean hasEntries(MultiMap map)
    {
        return map != null && !map.isEmpty();
    }

    /**
     * Build the URL for the specified request
     * @param request the data service request
     * @return The URL string
     */
    protected String buildUrl(Request request)
    {
        StringBuilder sb = new StringBuilder(512);
        sb.append(baseUrl).append(request.getEntityName()).append("/");
        if(request.getPath() != null)
            sb.append(request.getPath());

        String queryString = buildQueryString(request);
        if(queryString.length()>0)
        {
            sb.append("?").append(queryString);
        }

        return sb.toString();
    }

    /**
     * Build the query string for the specified request
     * @param request the data service request
     * @return The query string
     */
    protected String buildQueryString(Request request)
    {
        MultiMap resolvedParameters = null;
        if(parameterFactory != null)
        {
            resolvedParameters = parameterFactory.getParameters();
        }

        if(hasEntries(resolvedParameters))
            resolvedParameters = resolvedParameters.merge(request.getParameters());
        else
            resolvedParameters = request.getParameters();

        if(authorizationFactory != null)
        {
            MultiMap authEntries = authorizationFactory.getParameters();
            if(hasEntries(authEntries))
                resolvedParameters = resolvedParameters.merge(authEntries);
        }


        int ct = 0;
        StringBuilder sb = new StringBuilder(512);
        for (Map.Entry<String, List<String>> entry : resolvedParameters.entries())
        {
            for (String value : entry.getValue())
            {
                if(ct++ > 0)
                    sb.append("&");
                sb.append(encode(entry.getKey())).append("=").append(encode(value));
            }
        }

        return sb.toString();
    }

    /**
     * Return the headers for the supplied request
     * @param request The data service request
     * @return The headers
     */
    protected Headers getHeaders(Request request)
    {
        Headers.Builder headers = new Headers.Builder();
        MultiMap resolvedHeaders = null;
        if(headerFactory != null)
            resolvedHeaders = headerFactory.getHeaders();

        if(hasEntries(resolvedHeaders))
            resolvedHeaders = resolvedHeaders.merge(request.getHeaders());
        else
            resolvedHeaders = request.getHeaders();

        if(authorizationFactory != null)
        {
            MultiMap authEntries = authorizationFactory.getHeaders();
            if(hasEntries(authEntries))
                resolvedHeaders = resolvedHeaders.merge(authEntries);
        }

        if(resolvedHeaders.getFirst("user-agent") == null)
            headers.add("user-agent",userAgent);

        if(resolvedHeaders.getFirst(CLIENT_VERSION_HEADER_NAME) == null)
            headers.add(CLIENT_VERSION_HEADER_NAME,getClientVersion());

        for (Map.Entry<String, List<String>> entry : resolvedHeaders.entries())
        {
            for (String value : entry.getValue())
            {
                headers.add(entry.getKey(), value);
            }
        }
        return headers.build();
    }

    /**
     * Get the client version to use in the request header
     * @return The client version
     */
    protected String getClientVersion()
    {
        return CLIENT_VERSION;
    }

    /**
     * Encode the supplied string
     * @param value The string
     * @return The enclded value
     */
    protected String encode(String value)
    {
        try
        {
            return URLEncoder.encode(value,"UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new ClientException(500,"Error encoding parameter.",e);
        }
    }

    /**
     * Build the low level http client to use to call the service
     * @param trustAllCertificates A flag indicating if this client should trust all certificates. Note, this should only
     *                             be set to true when calling services using self signed certificates.
     * @return The http client
     */
    protected OkHttpClient buildClient(boolean trustAllCertificates)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        if( trustAllCertificates)
        {
            try
            {
                // Create a trust manager that does not validate certificate chains
                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager()
                        {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                                    throws CertificateException
                            {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
                                    throws
                                    CertificateException
                            {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers()
                            {
                                return null;
                            }
                        }
                };

                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                okHttpClient.setSslSocketFactory(sslSocketFactory);
                okHttpClient.setHostnameVerifier(new HostnameVerifier()
                {
                    @Override
                    public boolean verify(String hostname, SSLSession session)
                    {
                        return true;
                    }
                });

                return okHttpClient;
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }

        return okHttpClient;

    }

    private static class ObjectMapperBuilder
    {
        private final ObjectMapper objectMapper;

        private ObjectMapperBuilder()
        {
            objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        }

        private ObjectMapper getObjectMapper()
        {
            return objectMapper;
        }
    }

    private void copy(InputStream is, OutputStream os) throws IOException
    {
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = is.read(buffer)))
        {
            os.write(buffer, 0, n);
        }
    }

}
