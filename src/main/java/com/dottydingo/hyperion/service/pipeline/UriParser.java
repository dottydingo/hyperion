package com.dottydingo.hyperion.service.pipeline;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 */
public class UriParser
{
    private Pattern queryPattern = Pattern.compile("^/([a-zA-Z]+)/?$");
    private Pattern idPattern = Pattern.compile("^/([a-zA-Z]+)(/history)?/(\\p{Alnum}+)$");

    public UriRequestResult parseRequestUri(String uri)
    {
        // most explicit first
        Matcher matcher = idPattern.matcher(uri);
        if(matcher.find())
        {
            UriRequestResult result = new UriRequestResult();
            result.setEndpoint(matcher.group(1));
            result.setHistory(matcher.group(2) != null);
            result.setId(matcher.group(3));

            return result;
        }

        matcher = queryPattern.matcher(uri);
        if(matcher.find())
        {
            UriRequestResult result = new UriRequestResult();
            result.setEndpoint(matcher.group(1));
            result.setHistory(false);
            result.setId(null);

            return result;
        }

        return null;
    }
}
