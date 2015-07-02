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

import javax.net.ssl.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;

/**
 */
public class HyperionClient
{
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapperBuilder().getObjectMapper();
    protected String baseUrl;
    protected OkHttpClient client;
    protected ObjectMapper objectMapper = DEFAULT_OBJECT_MAPPER;
    protected ParameterFactory parameterFactory;
    protected HeaderFactory headerFactory;
    protected AuthorizationFactory authorizationFactory;
    protected ClientEventListener clientEventListener;
    protected String userAgent = "hyperionClient";

    public HyperionClient(String baseUrl)
    {
        this(baseUrl,false);
    }

    public HyperionClient(String baseUrl, boolean trustAllCertificates)
    {
        this.baseUrl = baseUrl;
        if (!baseUrl.endsWith("/"))
            this.baseUrl+="/";

        client = buildClient(trustAllCertificates);
    }

    public HyperionClient(String baseUrl, AuthorizationFactory authorizationFactory)
    {
        this(baseUrl,authorizationFactory,false);
    }

    public HyperionClient(String baseUrl, AuthorizationFactory authorizationFactory,boolean trustAllCertificates)
    {
        this(baseUrl,trustAllCertificates);
        this.authorizationFactory = authorizationFactory;
    }

    public void setParameterFactory(ParameterFactory parameterFactory)
    {
        this.parameterFactory = parameterFactory;
    }

    public void setHeaderFactory(HeaderFactory headerFactory)
    {
        this.headerFactory = headerFactory;
    }

    public void setObjectMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

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

    public void setKeepAliveConfiguration(KeepAliveConfiguration keepAliveConfiguration)
    {
        if(keepAliveConfiguration != null)
            client.setConnectionPool(new ConnectionPool(keepAliveConfiguration.getMaxIdleConnections(),
                    keepAliveConfiguration.getKeepAliveDurationMs()));
    }

    public void setClientEventListener(ClientEventListener clientEventListener)
    {
        this.clientEventListener = clientEventListener;
    }

    public void setUserAgent(String userAgent)
    {
        this.userAgent = userAgent;
    }

    public <T extends ApiObject> EntityResponse<T> get(Request<T> request)
    {
        return executeRequest(request,objectMapper.getTypeFactory()
                .constructParametricType(EntityResponse.class, request.getEntityType()));
    }

    public <T extends ApiObject> EntityResponse<T> query(Request<T> request)
    {
        return executeRequest(request, objectMapper.getTypeFactory()
                .constructParametricType(EntityResponse.class, request.getEntityType()));
    }

    public int delete(Request request)
    {
        DeleteResponse response = executeRequest(request,objectMapper.getTypeFactory().constructType(
                DeleteResponse.class));
        return response.getCount();
    }

    public <T extends ApiObject> T create(Request<T> request)
    {
        return executeRequest(request,objectMapper.getTypeFactory().constructType(request.getEntityType()));
    }

    public <T extends ApiObject> EntityList<T> createCollection(Request<T> request)
    {
        return executeRequest(request,objectMapper.getTypeFactory()
                .constructParametricType(EntityList.class, request.getEntityType()));
    }

    public <T extends ApiObject> T update(Request<T> request)
    {
        return executeRequest(request, objectMapper.getTypeFactory().constructType(request.getEntityType()));
    }

    public <T extends ApiObject> EntityList<T> updateCollection(Request<T> request)
    {
        return executeRequest(request,objectMapper.getTypeFactory()
                .constructParametricType(EntityList.class, request.getEntityType()));
    }
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


    protected Response executeRequest(Request request)
    {
        try
        {
            com.squareup.okhttp.Request httpRequest = buildHttpRequest(request);
            Response response = client.newCall(httpRequest).execute();

            if(response.code() == HttpURLConnection.HTTP_UNAUTHORIZED && authorizationFactory != null
                    && authorizationFactory.retryOnAuthenticationError())
            {
                if(authorizationFactory instanceof ResettableAuthorizationFactory)
                    ((ResettableAuthorizationFactory) authorizationFactory).reset();

                response = client.newCall(httpRequest).execute();
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
            catch (Exception ignore)
            {
            }

            if (resolvedException == null)
            {
                resolvedException =
                        new HyperionException(errorResponse.getStatusCode(), errorResponse.getMessage());
            }

            resolvedException.setErrorDetails(errorResponse.getErrorDetails());
        }

        if (resolvedException == null)
        {
            resolvedException =
                    new HyperionException(response.code(), response.message());
        }

        return resolvedException;

    }

    protected boolean hasEntries(MultiMap map)
    {
        return map != null && !map.isEmpty();
    }
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

        for (Map.Entry<String, List<String>> entry : resolvedHeaders.entries())
        {
            for (String value : entry.getValue())
            {
                headers.add(entry.getKey(), value);
            }
        }
        return headers.build();
    }


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

}
