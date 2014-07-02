package com.dottydingo.hyperion.core.endpoint.pipeline.phase;

/**
 */
public class VersionUriParser implements UriParser
{
    public UriRequestResult parseRequestUri(String uri)
    {
        // entity format  /<endpoint>/v1(/)
        // id format /<endpoint>/v1/<ids>
        // history format /<endpoint>/v1/history/<id>

        String[] split = uri.split("/");
        if(split.length < 3)
            return null;

        String endpoint = null;
        String id = null;
        boolean history = false;
        String version = null;
        if(split.length < 4)
        {
            endpoint = split[1];
            version = split[2];
        }
        else if(split.length == 4)
        {
            endpoint = split[1];
            version = split[2];
            id = split[3].trim();
            if(id.length() == 0 || id.equals("history"))
                return null;
        }
        else if(split.length == 5 && split[3].equals("history"))
        {
            endpoint = split[1];
            version = split[2];
            history = true;
            id = split[4].trim();
            if(id.length() == 0)
                return null;
        }
        else
            return null;

        if(endpoint.length() == 0)
            return null;

        if(version.length() == 0 || !version.startsWith("v"))
            return null;

        if(version.length() == 1)
                return null;

        version = version.substring(1);

        UriRequestResult result = new UriRequestResult();
        result.setEndpoint(endpoint);
        result.setHistory(history);
        result.setId(id);
        result.setVersion(version);

        return result;

    }
}
