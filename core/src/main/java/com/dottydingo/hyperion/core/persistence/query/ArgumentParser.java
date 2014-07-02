package com.dottydingo.hyperion.core.persistence.query;

import com.dottydingo.hyperion.api.exception.HyperionException;

/**
 */
public interface ArgumentParser
{
    <T> T parse(String argument, Class<T> type) throws HyperionException;
}
