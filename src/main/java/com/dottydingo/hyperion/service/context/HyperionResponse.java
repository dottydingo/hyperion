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
            setHeader("Cache-Control","max-age=0, no-cache");
            setHeader("Pragma","no-cache");
        }
        else
        {
            setHeader("Cache-Control",String.format("max-age=%d",cacheMaxAge));
        }
    }

}
