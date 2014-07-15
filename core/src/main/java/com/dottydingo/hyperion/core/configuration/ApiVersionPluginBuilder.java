package com.dottydingo.hyperion.core.configuration;

import com.dottydingo.hyperion.api.ApiObject;
import com.dottydingo.hyperion.api.Endpoint;
import com.dottydingo.hyperion.core.persistence.CreateKeyProcessor;
import com.dottydingo.hyperion.core.registry.ApiVersionPlugin;
import com.dottydingo.hyperion.core.registry.EntityQueryBuilder;
import com.dottydingo.hyperion.core.registry.EntitySortBuilder;
import com.dottydingo.hyperion.core.translation.Translator;
import com.dottydingo.hyperion.core.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class ApiVersionPluginBuilder
{
    private static Logger logger = LoggerFactory.getLogger(ApiVersionPluginBuilder.class);

    protected Class<? extends ApiObject> apiClass;
    protected Translator translator;
    protected Validator validator;

    protected Map<String,EntitySortBuilder> sortBuilders = Collections.emptyMap();
    protected Map<String,EntityQueryBuilder> queryBuilders = Collections.emptyMap();
    protected CreateKeyProcessor createKeyProcessor;

    public ApiVersionPlugin build(EntityPluginBuilder entityPluginBuilder) throws Exception
    {
        if(createKeyProcessor == null)
            createKeyProcessor = entityPluginBuilder.getDefaultCreateKeyProcessor();

        validateRequired();

        Endpoint endpoint = getEndpointAnnotation(apiClass);

        ApiVersionPlugin plugin = new ApiVersionPlugin();
        plugin.setVersion(endpoint.version());
        plugin.setApiClass(apiClass);
        plugin.setTranslator(translator);
        plugin.setValidator(validator);
        plugin.setCreateKeyProcessor(createKeyProcessor);

        Map<String,EntitySortBuilder> sorts = new HashMap<>();
        sorts.putAll(entityPluginBuilder.getSortBuilders());
        sorts.putAll(getSortBuilder(sortBuilders));
        plugin.setSortBuilders(sorts);
        if(sorts.isEmpty())
            logger.warn("No sortBuilders specified for apiClass: {}",apiClass);

        Map<String,EntityQueryBuilder> queries = new HashMap<>();
        queries.putAll(entityPluginBuilder.getQueryBuilders());
        queries.putAll(getQueryBuilders(queryBuilders));
        plugin.setQueryBuilders(queries);
        if(queries.isEmpty())
            logger.warn("No queryBuilders specified for apiClass: {}",apiClass);

        logger.debug("Plugin: {}",plugin);

        return plugin;
    }

    protected void validateRequired()
    {
        if(apiClass == null)
            throw new RuntimeException("apiClass must be specified");

        if(translator == null)
            throw new RuntimeException("translator must be specified");

        if(validator == null)
            throw new RuntimeException("validator must be specified");
    }

    protected Map<String,EntitySortBuilder> getSortBuilder(Map<String,EntitySortBuilder> sortBuilders)
    {
        return sortBuilders;
    }

    protected Map<String,EntityQueryBuilder> getQueryBuilders(Map<String,EntityQueryBuilder> queryBuilders)
    {
        return queryBuilders;
    }

    protected Endpoint getEndpointAnnotation(Class<? extends ApiObject> apiClass)
    {
        Endpoint endpoint = apiClass.getAnnotation(Endpoint.class);
        if(endpoint == null)
            throw new RuntimeException(String.format("Missing @Endpoint annotation in apiClass %s",apiClass));

        return endpoint;
    }


    /**
     * Set the API class for this version
     * @param apiClass the API class
     */
    public void setApiClass(Class<? extends ApiObject> apiClass)
    {
        this.apiClass = apiClass;
    }

    /**
     * Set the translator for this version
     * @param translator the translator
     */
    public void setTranslator(Translator translator)
    {
        this.translator = translator;
    }

    /**
     * Set the validator for this version
     * @param validator the validator
     */
    public void setValidator(Validator validator)
    {
        this.validator = validator;
    }

    /**
     * Set the sort builders for this version
     * @param sortBuilders the sort builders
     */
    public void setSortBuilders(Map<String, EntitySortBuilder> sortBuilders)
    {
        this.sortBuilders = sortBuilders;
    }

    /**
     * Set the query builders for this version
     * @param queryBuilders the query builders
     */
    public void setQueryBuilders(Map<String, EntityQueryBuilder> queryBuilders)
    {
        this.queryBuilders = queryBuilders;
    }

    /**
     * Set an optional create key processor for this version
     * @param createKeyProcessor the create key processor
     */
    public void setCreateKeyProcessor(CreateKeyProcessor createKeyProcessor)
    {
        this.createKeyProcessor = createKeyProcessor;
    }
}
