package com.dottydingo.hyperion.core;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 */
public class Util
{
    public static <T> List<T> asList(T... items)
    {
        return Arrays.asList(items);
    }

    public static <T> Set<T> asSet(T... items)
    {
        return new HashSet<>(Arrays.asList(items));
    }
}
