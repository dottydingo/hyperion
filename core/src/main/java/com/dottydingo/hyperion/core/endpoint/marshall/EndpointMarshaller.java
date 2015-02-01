package com.dottydingo.hyperion.core.endpoint.marshall;

import com.dottydingo.hyperion.core.configuration.HyperionEndpointConfiguration;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.ContextAttributes;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

/**
 */
public class EndpointMarshaller
{
    private HyperionEndpointConfiguration configuration;

    private ObjectMapper objectMapper;
    private ObjectMapper trackingObjectMapper;
    private boolean trackProvidedFieldsOnUpdate;

    public void init()
    {
        objectMapper = new ObjectMapper();

        baseConfigureObjectMapper(objectMapper);
        objectMapper.registerModule(new CaseInsensitiveEnumModule());

        trackProvidedFieldsOnUpdate = configuration.isTrackProvidedFieldsOnUpdate();
        if(trackProvidedFieldsOnUpdate)
        {
            // we need to create a new instance here since making a copy would share the module
            // registration
            trackingObjectMapper =new ObjectMapper();
            baseConfigureObjectMapper(trackingObjectMapper);
            trackingObjectMapper.registerModule(new CollectorModule());
        }

    }

    public void setConfiguration(HyperionEndpointConfiguration configuration)
    {
        this.configuration = configuration;
    }

    private void baseConfigureObjectMapper(ObjectMapper objectMapper)
    {
        // do extra bits first
        configureObjectMapper(objectMapper);

        // ensure that the settings we need are always in place
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(SerializationFeature.CLOSE_CLOSEABLE);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    }

    /**
     * Allow subclasses to provide additional object mapper configuration
     * @param objectMapper the mapper to configure
     */
    protected void configureObjectMapper(ObjectMapper objectMapper)
    {

    }

    public <T> T unmarshall(InputStream inputStream, Class<T> type) throws MarshallingException
    {
        try
        {
            return objectMapper.readValue(inputStream,type);
        }
        catch (Exception e)
        {
            throw new MarshallingException(e);
        }
    }

    public <T> RequestContext<T> unmarshallWithContext(InputStream inputStream, Class<T> type)  throws MarshallingException
    {

        if(!trackProvidedFieldsOnUpdate)
        {
            T item = unmarshall(inputStream,type);
            return new RequestContext<>(item,null);
        }

        try
        {
            Map<Object,Set<String>> providedFieldsMap = new IdentityHashMap<>();

            ContextAttributes attrs = trackingObjectMapper
                    .getDeserializationConfig()
                    .getAttributes()
                    .withPerCallAttribute(TrackingSettableBeanProperty.PROVIDED_FIELDS_MAP, providedFieldsMap);

            T value = trackingObjectMapper
                    .reader(attrs)
                    .readValue(trackingObjectMapper.getFactory().createParser(inputStream),type);


            return new RequestContext<T>(value,providedFieldsMap);
        }
        catch (Exception e)
        {
            throw new MarshallingException(e);
        }
    }


    public <T> void marshall(OutputStream outputStream, T value) throws MarshallingException
    {
        try
        {
            objectMapper.writeValue(outputStream,value);
        }
        catch(Exception e)
        {
            throw new MarshallingException(e);
        }

    }
}
