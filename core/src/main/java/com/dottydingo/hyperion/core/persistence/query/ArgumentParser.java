package com.dottydingo.hyperion.core.persistence.query;

import com.dottydingo.hyperion.api.exception.HyperionException;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;

import java.util.List;

/**
 */
public interface ArgumentParser
{
    <T> T parse(String argument, Class<T> type, PersistenceContext context) throws HyperionException;

    <T> List<T> parse(List<String> argument, Class<T> type, PersistenceContext context) throws HyperionException;
}
