package com.dottydingo.hyperion.client.builder;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.Endpoint;
import com.dottydingo.hyperion.client.builder.query.QueryExpression;
import com.dottydingo.hyperion.client.exception.ClientException;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Factory for creating request builders for a specific entity
 */
public class RequestFactory<T extends ApiObject<ID>,ID extends Serializable>
{
    private int version;
    private Class<T> type;
    private String entityName;

    /**
     * Create a request factory using the supplied API type.
     * @param type The API type, must have an @Endpoint annotation.
     */
    public RequestFactory(Class<T> type)
    {
        Endpoint endpoint = getEndpointAnnotation(type);
        this.version = endpoint.version();
        this.type = type;
        this.entityName = endpoint.value();
    }

    /**
     * Create a request factory using a specified version, API type, and entity name. Does not require that the API type
     * have an @Endpoint annotation
     * @param version The version
     * @param type The type
     * @param entityName The entity name
     */
    public RequestFactory(int version, Class<T> type, String entityName)
    {
        this.version = version;
        this.type = type;
        this.entityName = entityName;
    }

    /**
     * Create a request builder for a create operation for a single item
     * @param item The item to create
     * @return The request builder
     */
    public CreateSingleRequestBuilder<T,ID> create(T item)
    {
        return new CreateSingleRequestBuilder<>(version,type,entityName,item);
    }

    /**
     * Create a request builder for a create operation for a multiple items
     * @param items The items to create
     * @return The request builder
     */
    public CreateCollectionRequestBuilder<T,ID> create(List<T> items)
    {
        return new CreateCollectionRequestBuilder<T,ID>(version,type,entityName,items);
    }

    /**
     * Create a request builder for a delete operation using the specified IDs
     * @param ids The ids to delete
     * @return The request builder
     */
    public DeleteRequestBuilder<T,ID> delete(ID... ids)
    {
        return new DeleteRequestBuilder<T, ID>(version,type,entityName,ids);
    }

    /**
     * Create a request builder for a delete operation using the specified IDs
     * @param ids The ids to delete
     * @return The request builder
     */
    public DeleteRequestBuilder<T,ID> delete(List<ID> ids)
    {
        return new DeleteRequestBuilder<T, ID>(version,type,entityName, (ID[]) ids.toArray(new Serializable[ids.size()]));
    }

    /**
     * Create a request builder for a query operation using the specified query RQL string
     * @param query The the query
     * @return The request builder
     */
    public QueryRequestBuilder<T,ID> query(String query)
    {
        return new QueryRequestBuilder<T, ID>(version,type,entityName,query);
    }

    /**
     * Create a request builder for a query operation using the specified query expression
     * @param query The the query expression
     * @return The request builder
     */
    public QueryRequestBuilder<T,ID> query(QueryExpression query)
    {
        return new QueryRequestBuilder<T, ID>(version,type,entityName,query.build());
    }

    /**
     * Create a request builder for a query operation using the specified no query
     * @return The request builder
     */
    public QueryRequestBuilder<T,ID> query()
    {
        return new QueryRequestBuilder<T, ID>(version,type,entityName);
    }

    /**
     * Create a request builder for a find operation using the specified IDs
     * @param ids The ids to find
     * @return The request builder
     */
    public GetRequestBuilder<T,ID> find(ID... ids)
    {
        return new GetRequestBuilder<T, ID>(version,type,entityName,ids);
    }

    /**
     * Create a request builder for an update operation for a single item
     * @param item The item to update
     * @return The request builder
     */
    public UpdateSingleRequestBuilder<T,ID> update(T item)
    {
        return new UpdateSingleRequestBuilder<>(version,type,entityName,item);
    }

    /**
     * Create a request builder for a create operation for a multiple items
     * @param items The item to update
     * @return The request builder
     */
    public UpdateCollectionRequestBuilder<T,ID> update(List<T> items)
    {
        return new UpdateCollectionRequestBuilder<T, ID>(version,type,entityName,items);
    }

    protected Endpoint getEndpointAnnotation(Class<? extends ApiObject> apiClass)
    {
        Endpoint endpoint = apiClass.getAnnotation(Endpoint.class);
        if(endpoint == null)
            throw new ClientException(String.format("Missing @Endpoint annotation in apiClass %s",apiClass));

        return endpoint;
    }
}
