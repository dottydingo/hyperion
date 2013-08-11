package com.dottydingo.hyperion.service.context;

import com.dottydingo.service.endpoint.context.EndpointResponse;

/**
 */
public class HyperionResponse extends EndpointResponse
{

    public void setCacheMaxAge(int cacheMaxAge)
    {
        if(cacheMaxAge < 0)
            return;

        if(cacheMaxAge == 0)
        {
            setHeader("cache-control","max-age=0, no-cache");
            setHeader("pragma","no-cache");
        }
        else
        {
            setHeader("cache-control",String.format("max-age=%d",cacheMaxAge));
        }
    }

}
