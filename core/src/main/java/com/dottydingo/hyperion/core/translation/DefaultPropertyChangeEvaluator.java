package com.dottydingo.hyperion.core.translation;

/**
 * A default implementation of a PropertyChangeEvaluator that uses a simple equals check.
 */
public class DefaultPropertyChangeEvaluator<T> implements PropertyChangeEvaluator<T>
{
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasChanged(T oldValue, T newValue)
    {
        return !equals(oldValue,newValue);
    }

    private boolean equals(Object object1, Object object2)
    {
        if (object1 == object2)
        {
            return true;
        }
        if ((object1 == null) || (object2 == null))
        {
            return false;
        }
        return object1.equals(object2);
    }
}
