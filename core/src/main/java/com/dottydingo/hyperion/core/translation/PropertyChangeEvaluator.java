package com.dottydingo.hyperion.core.translation;

/**
 */
public interface PropertyChangeEvaluator<T>
{
    boolean hasChanged(T oldValue,T newValue);
}
