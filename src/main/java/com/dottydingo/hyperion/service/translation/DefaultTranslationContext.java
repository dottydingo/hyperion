package com.dottydingo.hyperion.service.translation;

import java.util.Set;

/**
 */
public class DefaultTranslationContext implements TranslationContext
{
    private Set<String> requestedFields;
    private String userString = "<not set>";

    public Set<String> getRequestedFields()
    {
        return requestedFields;
    }

    public void setRequestedFields(Set<String> requestedFields)
    {
        this.requestedFields = requestedFields;
    }

    public String getUserString()
    {
        return userString;
    }

    public void setUserString(String userString)
    {
        this.userString = userString;
    }
}
