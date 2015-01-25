package com.dottydingo.hyperion.core;

import com.dottydingo.hyperion.core.message.HyperionMessageSource;

import java.util.Locale;

/**
 */
public class StubMessageSource extends HyperionMessageSource
{
    @Override
    public String getErrorMessage(String code, Locale locale, Object... args)
    {
        return code;
    }

    @Override
    public String getValidationMessage(String code, Locale locale, Object... args)
    {
        return code;
    }
}
