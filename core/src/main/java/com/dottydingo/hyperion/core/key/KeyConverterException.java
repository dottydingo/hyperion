package com.dottydingo.hyperion.core.key;

/**
 */
public class KeyConverterException extends RuntimeException
{
    private String value;

    public KeyConverterException(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}
