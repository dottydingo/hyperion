package com.dottydingo.hyperion.core.registry;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.core.model.PersistentObject;
import com.dottydingo.hyperion.core.persistence.CreateKeyProcessor;
import com.dottydingo.hyperion.core.translation.Translator;
import com.dottydingo.hyperion.core.validation.Validator;

import java.util.Map;

/**
 */
public class ApiVersionPlugin<C extends ApiObject,P extends PersistentObject>
{
    private Integer version;
    private Translator<C,P> translator;
    private Validator<C,P> validator;
    private Class<C> apiClass;

    private Map<String,EntitySortBuilder> sortBuilders;
    private Map<String,EntityQueryBuilder> queryBuilders;
    private CreateKeyProcessor createKeyProcessor;

    public ApiVersionPlugin()
    {
    }

    public ApiVersionPlugin(Integer version, Translator<C, P> translator, Validator<C, P> validator, Class<C> apiClass)
    {
        this.version = version;
        this.translator = translator;
        this.validator = validator;
        this.apiClass = apiClass;
    }

    public Integer getVersion()
    {
        return version;
    }

    public void setVersion(Integer version)
    {
        this.version = version;
    }

    public Translator<C,P> getTranslator()
    {
        return translator;
    }

    public void setTranslator(Translator<C,P> translator)
    {
        this.translator = translator;
    }

    public Validator<C,P> getValidator()
    {
        return validator;
    }

    public void setValidator(Validator<C,P> validator)
    {
        this.validator = validator;
    }

    public Class<C> getApiClass()
    {
        return apiClass;
    }

    public void setApiClass(Class<C> apiClass)
    {
        this.apiClass = apiClass;
    }

    public Map<String, EntitySortBuilder> getSortBuilders()
    {
        return sortBuilders;
    }

    public void setSortBuilders(Map<String, EntitySortBuilder> sortBuilders)
    {
        this.sortBuilders = sortBuilders;
    }

    public Map<String, EntityQueryBuilder> getQueryBuilders()
    {
        return queryBuilders;
    }

    public void setQueryBuilders(Map<String, EntityQueryBuilder> queryBuilders)
    {
        this.queryBuilders = queryBuilders;
    }

    public CreateKeyProcessor getCreateKeyProcessor()
    {
        return createKeyProcessor;
    }

    public void setCreateKeyProcessor(CreateKeyProcessor createKeyProcessor)
    {
        this.createKeyProcessor = createKeyProcessor;
    }
}
