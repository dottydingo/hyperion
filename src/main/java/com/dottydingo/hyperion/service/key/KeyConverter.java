package com.dottydingo.hyperion.service.key;


import java.io.Serializable;
import java.util.List;

/**
 * Convert incoming key values from a String to the native PK type.
 */
public interface KeyConverter<T extends Serializable>
{
    /**
     * Convert a comma separated list of values into a List of the PK type.
     * @param idValues The incoming id values. Multiple values can be comma separated. Implementations should
     *                 be sure to clean up any extra whitespace between values.
     * @return A list of PK values
     */
    List<T> covertKeys(String idValues);
}
