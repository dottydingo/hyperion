package com.dottydingo.hyperion.core.configuration;

import com.dottydingo.hyperion.core.endpoint.HttpMethod;
import com.dottydingo.hyperion.core.key.IntegerKeyConverter;
import com.dottydingo.hyperion.core.key.KeyConverter;
import com.dottydingo.hyperion.core.key.LongKeyConverter;
import com.dottydingo.hyperion.core.key.StringKeyConverter;
import com.dottydingo.hyperion.core.model.PersistentHistoryEntry;
import com.dottydingo.hyperion.core.model.PersistentObject;
import com.dottydingo.hyperion.core.persistence.*;
import com.dottydingo.hyperion.core.persistence.dao.Dao;
import com.dottydingo.hyperion.core.persistence.event.PersistentChangeListener;
import com.dottydingo.hyperion.core.registry.*;
import com.fasterxml.classmate.MemberResolver;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.ResolvedTypeWithMembers;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.ResolvedField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Convenience class for building an entity plugin
 */
public class EntityPluginBuilder
{
    private static final KeyConverter STRING_KEY_CONVERTER = new StringKeyConverter();
    private static final KeyConverter LONG_KEY_CONVERTER = new LongKeyConverter();
    private static final KeyConverter INTEGER_KEY_CONVERTER = new IntegerKeyConverter();

    private Logger logger = logger = LoggerFactory.getLogger(EntityPluginBuilder.class);

    protected String endpointName;
    protected Class<? extends PersistentObject> entityClass;
    protected KeyConverter keyConverter;

    protected HttpMethod[] limitMethods;
    protected int cacheMaxAge = 0;

    protected PersistenceOperations persistenceOperations;
    protected Dao dao;
    protected PersistenceFilter<PersistentObject> persistenceFilter;

    protected Boolean historyEnabled;
    protected Class<? extends PersistentHistoryEntry> historyType;

    protected CreateKeyProcessor defaultCreateKeyProcessor;

    protected List<PersistentChangeListener> persistentChangeListeners = new ArrayList<>();
    protected List<PersistentChangeListener> entityChangeListeners = new ArrayList<>();

    protected Map<String,EntitySortBuilder> defaultSortBuilders = new HashMap<>();
    protected Map<String,EntityQueryBuilder> defaultQueryBuilders = new HashMap<>();

    protected Map<String,EntitySortBuilder> overrideSortBuilders = new HashMap<>();
    protected Map<String,EntityQueryBuilder> overrideQueryBuilders = new HashMap<>();

    protected String[] additionalParameters = new String[0];

    protected List<ApiVersionPluginBuilder> versions = new ArrayList<>();


    public EntityPlugin build(ServiceRegistryBuilder serviceRegistryBuilder) throws Exception
    {
        if(keyConverter == null)
            keyConverter = serviceRegistryBuilder.getDefaultKeyConverter();

        if(persistenceOperations == null)
            serviceRegistryBuilder.getDefaultPersistenceOperations();

        if(dao == null)
            dao = serviceRegistryBuilder.getDefaultDao();

        if(historyEnabled == null)
            historyEnabled = serviceRegistryBuilder.getDefaultHistoryEnabled();

        if(historyType == null)
            historyType = serviceRegistryBuilder.getDefaultHistoryType();

        validateRequired();

        EntityPlugin entityPlugin = new EntityPlugin();
        entityPlugin.setEndpointName(endpointName);
        entityPlugin.setEntityClass(entityClass);
        entityPlugin.setKeyConverter(getKeyConverter(keyConverter));
        if(entityPlugin.getKeyConverter() == null)
            throw new RuntimeException("keyConverter must be specified");

        if(limitMethods != null && limitMethods.length > 0)
            entityPlugin.setLimitMethods(new HashSet<HttpMethod>(Arrays.asList(limitMethods)));

        entityPlugin.setCacheMaxAge(cacheMaxAge);

        entityPlugin.setPersistenceOperations(persistenceOperations);
        entityPlugin.setDao(dao);
        entityPlugin.setPersistenceFilter(getPersistenceFilter(persistenceFilter));

        if(additionalParameters != null && additionalParameters.length > 0)
            entityPlugin.setAdditionalParameters(new LinkedHashSet<String>(Arrays.asList(additionalParameters)));

        if(historyEnabled != null)
            entityPlugin.setHistoryEnabled(historyEnabled);
        entityPlugin.setHistoryType(historyType);

        List<PersistentChangeListener> persistentListeners = new ArrayList<>();
        persistentListeners.addAll(serviceRegistryBuilder.getPersistentChangeListeners());
        persistentListeners.addAll(persistentChangeListeners);
        entityPlugin.setPersistentChangeListeners(persistentListeners);

        List<PersistentChangeListener> entityListeners = new ArrayList<>();
        entityListeners.addAll(serviceRegistryBuilder.getEntityChangeListeners());
        entityListeners.addAll(entityChangeListeners);
        entityPlugin.setEntityChangeListeners(entityListeners);


        List<ApiVersionPlugin> apiVersionPlugins = new ArrayList<ApiVersionPlugin>();
        for (ApiVersionPluginBuilder versionBuilder : versions)
        {
            apiVersionPlugins.add(versionBuilder.build(this));
        }
        ApiVersionRegistry apiVersionRegistry = new ApiVersionRegistry();
        apiVersionRegistry.setPlugins(apiVersionPlugins);
        entityPlugin.setApiVersionRegistry(apiVersionRegistry);

        applyAdditionalConfiguration(serviceRegistryBuilder,entityPlugin);
        return entityPlugin;
    }

    protected void applyAdditionalConfiguration(ServiceRegistryBuilder serviceRegistryBuilder,
                                                EntityPlugin plugin){}

    protected void validateRequired()
    {
        if(endpointName == null)
            throw new RuntimeException("endpointName must be specified");

        if(entityClass == null)
            throw new RuntimeException("entityClass must be specified");

        if(persistenceOperations == null)
            throw new RuntimeException("persistenceOperations must be specified");

        if(dao == null)
            throw new RuntimeException("dao must be specified");

        if(versions == null || versions.size() == 0)
            throw new RuntimeException("versions must be specified");

        if(historyEnabled != null && historyEnabled && historyType == null)
            throw new RuntimeException("historyType must be specified when history is enabled.");

    }

    protected KeyConverter getKeyConverter(KeyConverter keyConverter)
    {
        if(keyConverter != null)
            return keyConverter;

        TypeResolver typeResolver = new TypeResolver();
        ResolvedType resolvedType = typeResolver.resolve(entityClass);

        MemberResolver memberResolver = new MemberResolver(typeResolver);
        ResolvedTypeWithMembers typeWithMembers =memberResolver.resolve(resolvedType, null, null);

        Class<?> idType = null;
        for (ResolvedField field : typeWithMembers.getMemberFields())
        {
            if(field.getRawMember().getName().equals("id"))
            {
                idType = field.getType().getErasedType();
                break;
            }
        }

        if(idType == null)
        {
            logger.debug("Could not resolve the id field type");
            return null;
        }

        if(idType.equals(String.class))
            return STRING_KEY_CONVERTER;
        else if(idType.equals(Long.class))
            return LONG_KEY_CONVERTER;
        else if(idType.equals(Integer.class))
            return INTEGER_KEY_CONVERTER;

        return null;
    }

    protected PersistenceFilter getPersistenceFilter(PersistenceFilter persistenceFilter)
    {
        if(persistenceFilter == null)
            return new EmptyPersistenceFilter();

        return persistenceFilter;
    }

    protected String getEndpointName()
    {
        return endpointName;
    }

    /**
     * Set the endpoint name for the entity
     * @param endpointName the endpoint name
     */
    public EntityPluginBuilder setEndpointName(String endpointName)
    {
        this.endpointName = endpointName;
        return this;
    }

    protected Class<? extends PersistentObject> getEntityClass()
    {
        return entityClass;
    }

    /**
     * Set the entity persistent object type
     * @param entityClass the entity persistent object type
     */
    public EntityPluginBuilder setEntityClass(Class<? extends PersistentObject> entityClass)
    {
        this.entityClass = entityClass;
        return this;
    }

    protected KeyConverter getKeyConverter()
    {
        return keyConverter;
    }

    /**
     * Set the key converter for the entity
     * @param keyConverter the key converter
     */
    public EntityPluginBuilder setKeyConverter(KeyConverter keyConverter)
    {
        this.keyConverter = keyConverter;
        return this;
    }

    protected HttpMethod[] getLimitMethods()
    {
        return limitMethods;
    }

    /**
     * Set the methods that will be allowed on the endpoint. No setting this enables all request methods.
     * @param limitMethods The methods
     */
    public EntityPluginBuilder setLimitMethods(HttpMethod... limitMethods)
    {
        this.limitMethods = limitMethods;
        return this;
    }

    protected int getCacheMaxAge()
    {
        return cacheMaxAge;
    }

    /**
     * Set the maximum cache age to set for this entity. Defaults to 0.
     * @param cacheMaxAge the maximum cache age
     */
    public EntityPluginBuilder setCacheMaxAge(int cacheMaxAge)
    {
        this.cacheMaxAge = cacheMaxAge;
        return this;
    }

    protected PersistenceOperations getPersistenceOperations()
    {
        return persistenceOperations;
    }

    /**
     * Set the persistence operations instance to use for this entity
     * @param persistenceOperations the persistence operations instance
     */
    public EntityPluginBuilder setPersistenceOperations(PersistenceOperations persistenceOperations)
    {
        this.persistenceOperations = persistenceOperations;
        return this;
    }

    protected Dao getDao()
    {
        return dao;
    }

    /**
     * Set the Dao to use for this entity
     * @param dao the dao
     */
    public EntityPluginBuilder setDao(Dao dao)
    {
        this.dao = dao;
        return this;
    }

    protected PersistenceFilter<PersistentObject> getPersistenceFilter()
    {
        return persistenceFilter;
    }

    /**
     * Set the persistence filter to use for this entity
     * @param persistenceFilter the persistence filter
     */
    public EntityPluginBuilder setPersistenceFilter(PersistenceFilter<PersistentObject> persistenceFilter)
    {
        this.persistenceFilter = persistenceFilter;
        return this;
    }

    protected CreateKeyProcessor getDefaultCreateKeyProcessor()
    {
        return defaultCreateKeyProcessor;
    }

    /**
     * Set the default create key processor to use for versions of this entity. This instance will be
     * used unless one is specified for a specific version.
     * @param defaultCreateKeyProcessor the create key processor
     */
    public EntityPluginBuilder setDefaultCreateKeyProcessor(CreateKeyProcessor defaultCreateKeyProcessor)
    {
        this.defaultCreateKeyProcessor = defaultCreateKeyProcessor;
        return this;
    }

    protected List<PersistentChangeListener> getPersistentChangeListeners()
    {
        return persistentChangeListeners;
    }

    /**
     * Set the transactional entity change listeners to use for this entity. These will be added to any
     * persistentChangeListeners specified at the registry level.
     * @param persistentChangeListeners the entity change listeners
     */
    public EntityPluginBuilder setPersistentChangeListeners(List<PersistentChangeListener> persistentChangeListeners)
    {
        this.persistentChangeListeners = persistentChangeListeners;
        return this;
    }

    protected List<PersistentChangeListener> getEntityChangeListeners()
    {
        return entityChangeListeners;
    }

    /**
     * Set the post transaction entity change listeners to use for this entity. These will be added to any
     * persistentChangeListeners specified at the registry level.
     * @param entityChangeListeners the entity change listeners
     */
    public EntityPluginBuilder setEntityChangeListeners(List<PersistentChangeListener> entityChangeListeners)
    {
        this.entityChangeListeners = entityChangeListeners;
        return this;
    }

    protected List<ApiVersionPluginBuilder> getVersions()
    {
        return versions;
    }

    protected Map<String, EntitySortBuilder> getDefaultSortBuilders()
    {
        return defaultSortBuilders;
    }

    /**
     * Set the default sort builders to be used across all versions of the entity
     * @param defaultSortBuilders the sort builders
     */
    public void setDefaultSortBuilders(Map<String, EntitySortBuilder> defaultSortBuilders)
    {
        this.defaultSortBuilders = defaultSortBuilders;
    }

    public EntityPluginBuilder addDefaultSortBuilder(String name,EntitySortBuilder builder)
    {
        defaultSortBuilders.put(name,builder);
        return this;
    }

    protected Map<String, EntityQueryBuilder> getDefaultQueryBuilders()
    {
        return defaultQueryBuilders;
    }

    /**
     * Set the default query builders to be used across all versions of the entity
     * @param defaultQueryBuilders the query builders
     */
    public void setDefaultQueryBuilders(Map<String, EntityQueryBuilder> defaultQueryBuilders)
    {
        this.defaultQueryBuilders = defaultQueryBuilders;
    }

    public EntityPluginBuilder addDefaultQueryBuilder(String name,EntityQueryBuilder builder)
    {
        defaultQueryBuilders.put(name,builder);
        return this;
    }

    public Map<String, EntitySortBuilder> getOverrideSortBuilders()
    {
        return overrideSortBuilders;
    }

    /**
     * Set the override sort builders to be used across all versions of the entity
     * @param overrideSortBuilders the sort builders
     */
    public void setOverrideSortBuilders(Map<String, EntitySortBuilder> overrideSortBuilders)
    {
        this.overrideSortBuilders = overrideSortBuilders;
    }

    public EntityPluginBuilder addOverrideSortBuilder(String name,EntitySortBuilder builder)
    {
        overrideSortBuilders.put(name,builder);
        return this;
    }

    /**
     * Set the override query builders be to used across all versions of the entity
     * @param overrideQueryBuilders the query builders
     */
    public void setOverrideQueryBuilders(Map<String, EntityQueryBuilder> overrideQueryBuilders)
    {
        this.overrideQueryBuilders = overrideQueryBuilders;
    }

    public Map<String, EntityQueryBuilder> getOverrideQueryBuilders()
    {
        return overrideQueryBuilders;
    }
    {
        this.overrideQueryBuilders = overrideQueryBuilders;
    }

    public EntityPluginBuilder addOverrideQueryBuilder(String name,EntityQueryBuilder builder)
    {
        overrideQueryBuilders.put(name,builder);
        return this;
    }

    /**
     * Set the flag indicating if history is enabled for this entity.
     * @param historyEnabled the flag
     */
    public void setHistoryEnabled(Boolean historyEnabled)
    {
        this.historyEnabled = historyEnabled;
    }

    /**
     * Set the history type to use. Must be specified if history is enabled.
     * @param historyType the type
     */
    public EntityPluginBuilder setHistoryType(Class<? extends PersistentHistoryEntry> historyType)
    {
        this.historyType = historyType;
        return this;
    }

    /**
     * Set the plugin versions
     * @param versions the versions
     */
    public void setVersions(List<ApiVersionPluginBuilder> versions)
    {
        this.versions = versions;
    }

    public EntityPluginBuilder addVersion(ApiVersionPluginBuilder versionPluginBuilder)
    {
        versions.add(versionPluginBuilder);
        return this;
    }

    /**
     * Set any additional parameters that should be captured from the request
     * @param additionalParameters The additional parameters
     */
    public EntityPluginBuilder setAdditionalParameters(String... additionalParameters)
    {
        this.additionalParameters = additionalParameters;
        return this;
    }
}
