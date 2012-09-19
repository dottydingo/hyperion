package com.dottydingo.hyperion.service.query;


/**
 * User: mark
 * Date: 9/16/12
 * Time: 3:48 PM
 */
public interface ArgumentParser
{
    <T> T parse(String argument, Class<T> type) throws IllegalArgumentException, ArgumentFormatException;
}
