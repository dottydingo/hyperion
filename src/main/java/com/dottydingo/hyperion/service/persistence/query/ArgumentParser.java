package com.dottydingo.hyperion.service.persistence.query;

import com.dottydingo.hyperion.exception.HyperionException;

/**
 */
public interface ArgumentParser
{
    <T> T parse(String argument, Class<T> type) throws HyperionException;
}
