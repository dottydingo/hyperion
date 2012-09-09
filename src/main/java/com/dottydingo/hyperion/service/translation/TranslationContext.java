package com.dottydingo.hyperion.service.translation;

import java.util.Set;

/**
 */
public interface TranslationContext
{
    Set<String> getRequestedFields();

    String getUserString();
}
