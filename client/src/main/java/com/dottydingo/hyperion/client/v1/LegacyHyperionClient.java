package com.dottydingo.hyperion.client.v1;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.EntityList;
import com.dottydingo.hyperion.api.EntityResponse;
import com.dottydingo.hyperion.api.Page;
import com.dottydingo.hyperion.api.exception.InternalException;
import com.dottydingo.hyperion.api.v1.LegacyEntityResponse;
import com.dottydingo.hyperion.client.AuthorizationFactory;
import com.dottydingo.hyperion.client.HyperionClient;
import com.dottydingo.hyperion.client.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * Client for making requests to Hyperion 1.x services. The client is thread safe and a single instance should generally
 * be used to access all endpoints on a service.
 */
public class LegacyHyperionClient extends HyperionClient
{
    /**
     * Create a client with the supplied parameters.
     * @param baseUrl The base URL for the service
     */
    public LegacyHyperionClient(String baseUrl)
    {
        super(baseUrl);
    }

    /**
     * Create a client with the supplied parameters.
     * @param baseUrl The base URL for the service
     * @param trustAllCertificates A flag indicating if all certificates should be trusted. This should only
     *                             be set to true when calling a service using a self signed certificate.
     */
    public LegacyHyperionClient(String baseUrl, boolean trustAllCertificates)
    {
        super(baseUrl, trustAllCertificates);
    }

    /**
     * Create a client with the supplied parameters.
     * @param baseUrl The base URL for the service
     * @param authorizationFactory The authorization factory to use for making requests
     */
    public LegacyHyperionClient(String baseUrl, AuthorizationFactory authorizationFactory)
    {
        super(baseUrl, authorizationFactory);
    }

    /**
     * Create a client with the supplied parameters.
     * @param baseUrl The base URL for the service
     * @param authorizationFactory The authorization factory to use for making requests
     * @param trustAllCertificates A flag indicating if all certificates should be trusted. This should only
     *                             be set to true when calling a service using a self signed certificate.
     */
    public LegacyHyperionClient(String baseUrl, AuthorizationFactory authorizationFactory, boolean trustAllCertificates)
    {
        super(baseUrl, authorizationFactory, trustAllCertificates);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends ApiObject> EntityResponse<T> query(Request<T> request)
    {
        LegacyEntityResponse<T> legacyResponse = executeRequest(request, objectMapper.getTypeFactory()
                .constructParametrizedType(LegacyEntityResponse.class, LegacyEntityResponse.class, request.getEntityType()));

        EntityResponse<T> response = new EntityResponse<T>();
        response.setEntries(legacyResponse.getEntries());
        Page page = new Page();
        response.setPage(page);
        page.setStart(legacyResponse.getStart());
        page.setResponseCount(legacyResponse.getResponseCount());
        page.setTotalCount(legacyResponse.getTotalCount());

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends ApiObject> EntityList<T> create(Request<T> request)
    {
        EntityList<T> requestList = (EntityList<T>) request.getRequestBody();
        if(requestList.getEntries().size() > 1)
            throw new InternalException("V1 calls can only have a single entry on create.");
        T item = requestList.getEntries().get(0);
        request.setRequestBody(item);

        T legacyResponse = executeRequest(request,objectMapper.getTypeFactory().constructType(request.getEntityType()));

        EntityList<T> response = new EntityList<>();
        List<T> list = new ArrayList<>();
        response.setEntries(list);
        list.add(legacyResponse);

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends ApiObject> EntityList<T> update(Request<T> request)
    {
        EntityList<T> requestList = (EntityList<T>) request.getRequestBody();
        if(requestList.getEntries().size() > 1)
            throw new InternalException("V1 calls can only have a single entry on update.");
        T item = requestList.getEntries().get(0);
        request.setRequestBody(item);
        request.setPath(item.getId().toString());

        T legacyResponse = executeRequest(request,objectMapper.getTypeFactory().constructType(request.getEntityType()));

        EntityList<T> response = new EntityList<>();
        List<T> list = new ArrayList<>();
        response.setEntries(list);
        list.add(legacyResponse);

        return response;
    }
}
