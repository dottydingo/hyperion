package com.dottydingo.hyperion.core.persistence.query;

/**
 */
public class SampleValueOf
{
    private String value;

    public SampleValueOf(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    public static SampleValueOf valueOf(String value)
    {
        return new SampleValueOf(value);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof SampleValueOf))
        {
            return false;
        }

        SampleValueOf that = (SampleValueOf) o;

        if (value != null ? !value.equals(that.value) : that.value != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return value != null ? value.hashCode() : 0;
    }
}
