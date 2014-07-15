package com.dottydingo.hyperion.core.endpoint.marshall;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 */
public class RequestContext<T>
{
    private T requestObject;
    private Map<Object,Set<String>> providedFields;

    public RequestContext(T requestObject, Map<Object, Set<String>> providedFields)
    {
        this.requestObject = requestObject;
        this.providedFields = providedFields;
    }

    public T getRequestObject()
    {
        return requestObject;
    }

    public void setRequestObject(T requestObject)
    {
        this.requestObject = requestObject;
    }

    public Map<Object, Set<String>> getProvidedFields()
    {
        return providedFields;
    }

    public void setProvidedFields(Map<Object, Set<String>> providedFields)
    {
        this.providedFields = providedFields;
    }

    public boolean isTrackingProvidedFields()
    {
        return providedFields != null;
    }
}
