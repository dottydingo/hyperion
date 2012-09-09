package com.dottydingo.hyperion.service.configuration;

import com.dottydingo.hyperion.api.BaseApiObject;
import com.dottydingo.hyperion.service.marshall.ApiMarshaller;
import com.dottydingo.hyperion.service.model.BasePersistentObject;
import com.dottydingo.hyperion.service.query.QueryHandler;
import com.dottydingo.hyperion.service.validation.Validator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 */
public class EntityPlugin<C extends BaseApiObject,P extends BasePersistentObject>
{
    private String endpointName;
    private Class apiClass;
    private JpaRepository<P,Serializable> jpaRepository;
    private QueryHandler queryHandler;
    private ApiMarshaller apiMarshaller;
    private ApiVersionRegistry<C,P> apiVersionRegistry;

    public String getEndpointName()
    {
        return endpointName;
    }

    public void setEndpointName(String endpointName)
    {
        this.endpointName = endpointName;
    }

    public Class getApiClass()
    {
        return apiClass;
    }

    public void setApiClass(Class apiClass)
    {
        this.apiClass = apiClass;
    }

    public JpaRepository<P,Serializable> getJpaRepository()
    {
        return jpaRepository;
    }

    public void setJpaRepository(JpaRepository<P,Serializable> jpaRepository)
    {
        this.jpaRepository = jpaRepository;
    }

    public QueryHandler getQueryHandler()
    {
        return queryHandler;
    }

    public void setQueryHandler(QueryHandler queryHandler)
    {
        this.queryHandler = queryHandler;
    }

    public ApiMarshaller getApiMarshaller()
    {
        return apiMarshaller;
    }

    public void setApiMarshaller(ApiMarshaller apiMarshaller)
    {
        this.apiMarshaller = apiMarshaller;
    }


    public ApiVersionRegistry<C,P> getApiVersionRegistry()
    {
        return apiVersionRegistry;
    }

    public void setApiVersionRegistry(ApiVersionRegistry<C,P> apiVersionRegistry)
    {
        this.apiVersionRegistry = apiVersionRegistry;
    }
}
