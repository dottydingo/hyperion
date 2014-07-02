package com.dottydingo.hyperion.client.builder;

import com.dottydingo.hyperion.api.ApiObject;

import java.io.Serializable;

/**
 */
public class RequestFactory<T extends ApiObject<ID>,ID extends Serializable>
{
    private int version;
    private Class<T> type;
    private String entityName;

    public RequestFactory(int version, Class<T> type)
    {
        this(version, type,type.getSimpleName());
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
}
