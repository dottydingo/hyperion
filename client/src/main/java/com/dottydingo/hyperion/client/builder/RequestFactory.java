package com.dottydingo.hyperion.client.builder;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.Endpoint;
import com.dottydingo.hyperion.client.builder.query.QueryExpression;

import java.io.Serializable;

/**
 */
public class RequestFactory<T extends ApiObject<ID>,ID extends Serializable>
{
    private int version;
    private Class<T> type;
    private String entityName;

    public RequestFactory(Class<T> type)
    {
        Endpoint endpoint = getEndpointAnnotation(type);
        this.version = endpoint.version();
        this.type = type;
        this.entityName = endpoint.value();
    }

    public RequestFactory(int version, Class<T> type, String entityName)
    {
        this.version = version;
        this.type = type;
        this.entityName = entityName;
    }

    public CreateRequestBuilder<T,ID> create(T item)
    {
        return new CreateRequestBuilder<T, ID>(version,type,entityName,item);
    }

    public DeleteRequestBuilder<T,ID> delete(ID... ids)
    {
        return new DeleteRequestBuilder<T, ID>(version,type,entityName,ids);
    }

    public QueryRequestBuilder<T,ID> query(String query)
    {
        return new QueryRequestBuilder<T, ID>(version,type,entityName,query);
    }

    public QueryRequestBuilder<T,ID> query(QueryExpression query)
    {
        return new QueryRequestBuilder<T, ID>(version,type,entityName,query.build());
    }

    public QueryRequestBuilder<T,ID> query()
    {
        return new QueryRequestBuilder<T, ID>(version,type,entityName);
    }

    public GetRequestBuilder<T,ID> find(ID... ids)
    {
        return new GetRequestBuilder<T, ID>(version,type,entityName,ids);
    }

    public UpdateRequestBuilder<T,ID> update(T item)
    {
        return new UpdateRequestBuilder<T, ID>(version,type,entityName,item);
    }

    protected Endpoint getEndpointAnnotation(Class<? extends ApiObject> apiClass)
    {
        Endpoint endpoint = apiClass.getAnnotation(Endpoint.class);
        if(endpoint == null)
            throw new RuntimeException(String.format("Missing @Endpoint annotation in apiClass %s",apiClass));

        return endpoint;
    }
}
