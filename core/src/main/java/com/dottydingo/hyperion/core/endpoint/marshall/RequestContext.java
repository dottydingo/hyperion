package com.dottydingo.hyperion.core.endpoint.marshall;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 */
public class RequestContext<T>
{
    private T requestObject;
    private Set<String> setFields = new HashSet<String>();

    public RequestContext(T requestObject, Iterator<String> iterator)
    {
        this.requestObject = requestObject;
        while (iterator.hasNext())
        {
            setFields.add(iterator.next());
        }
    }

    public T getRequestObject()
    {
        return requestObject;
    }

    public void setRequestObject(T requestObject)
    {
        this.requestObject = requestObject;
    }

    public Set<String> getSetFields()
    {
        return setFields;
    }

    public void setSetFields(Set<String> setFields)
    {
        this.setFields = setFields;
    }
}
