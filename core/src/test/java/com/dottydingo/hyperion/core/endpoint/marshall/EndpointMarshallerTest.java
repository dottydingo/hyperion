package com.dottydingo.hyperion.core.endpoint.marshall;

import com.dottydingo.hyperion.api.EntityResponse;
import com.dottydingo.hyperion.core.configuration.HyperionEndpointConfiguration;
import com.dottydingo.service.endpoint.configuration.EndpointConfiguration;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 */
public class EndpointMarshallerTest
{
    private EndpointMarshaller endpointMarshaller;
    private static ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);


    @Before
    public void setup()
    {
        endpointMarshaller = new EndpointMarshaller();
        HyperionEndpointConfiguration configuration = new HyperionEndpointConfiguration();
        endpointMarshaller.setConfiguration(configuration);
        endpointMarshaller.init();

    }
    @Test
    public void testUnmarshallCollection() throws Exception
    {
        EntityResponse<SampleClient> entityResponse = new EntityResponse<>();
        List<SampleClient> entries = new ArrayList<>();
        entityResponse.setEntries(entries);
        entries.add(buildClient(1L,"field1","field2"));
        entries.add(buildClient(2L,"field3","field4"));

        String json =  objectMapper.writeValueAsString(entityResponse);
        ByteArrayInputStream is = new ByteArrayInputStream(json.getBytes());

        List<SampleClient> response = endpointMarshaller.unmarshallCollection(is,SampleClient.class);
        assertNotNull(response);
        assertEquals(2,response.size());
        assertEquals(new Long(1),response.get(0).getId());
        assertEquals(new Long(2),response.get(1).getId());

    }

    @Test
    public void testUnmarshallCollection_Invalid() throws Exception
    {

        valdateError("","Missing start object token");
        valdateError("{}","Empty request payload");
        valdateError("{\"foo\": 1}","Payload missing \"entries\" field");
        valdateError("{\"entries\": 1}","The \"entries\" field must be an array");
    }

    private void valdateError(String input,String message)
    {
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        try
        {
            endpointMarshaller.unmarshallCollection(is,SampleClient.class);
            fail();
        }
        catch (MarshallingException e)
        {
            assertEquals(message,e.getLocalizedMessage());
        }
    }

    private SampleClient buildClient(Long id,String field1,String field2)
    {
        SampleClient client = new SampleClient();
        client.setId(id);
        client.setStringField(field1);
        client.setAnotherStringField(field2);
        return client;
    }
}