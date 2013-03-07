package com.dottydingo.hyperion.service.persistence.query;

/**
 */
public interface ArgumentParser<T>
{
    T parse(String value);
}
