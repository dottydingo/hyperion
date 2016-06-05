package com.dottydingo.hyperion.core.translation;

/**
 * Base interface to check for changes in a value.
 */
public interface PropertyChangeEvaluator<T>
{
    /**
     * Check to see if two values are different.
     * @param oldValue The original value
     * @param newValue The new value
     * @return True if the values are different, false otherwise
     */
    boolean hasChanged(T oldValue,T newValue);
}
