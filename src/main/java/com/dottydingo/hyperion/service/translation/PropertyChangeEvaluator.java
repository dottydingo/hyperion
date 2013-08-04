package com.dottydingo.hyperion.service.translation;

/**
 */
public interface PropertyChangeEvaluator<T>
{
    boolean hasChanged(T oldValue,T newValue);
}
