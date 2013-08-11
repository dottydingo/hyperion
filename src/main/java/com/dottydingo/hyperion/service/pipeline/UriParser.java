package com.dottydingo.hyperion.service.pipeline;

/**
 */
public class UriParser
{
    public UriRequestResult parseRequestUri(String uri)
    {
        // entity format  /<endpoint>(/)
        // id format /<endpoint>/<ids>
        // history format /<endpoint>/history/<id>

        String[] split = uri.split("/");
        if(split.length < 2)
            return null;

        String endpoint = null;
        String id = null;
        boolean history = false;
        if(split.length < 3)
        {
            endpoint = split[1];
        }
        else if(split.length == 3)
        {
            endpoint = split[1];
            id = split[2].trim();
            if(id.length() == 0 || id.equals("history"))
                return null;
        }
        else if(split.length == 4 && split[2].equals("history"))
        {
            endpoint = split[1];
            history = true;
            id = split[3].trim();
            if(id.length() == 0)
                return null;
        }
        else
            return null;

        if(endpoint.length() == 0)
            return null;

        UriRequestResult result = new UriRequestResult();
        result.setEndpoint(endpoint);
        result.setHistory(history);
        result.setId(id);

        return result;

    }
}
