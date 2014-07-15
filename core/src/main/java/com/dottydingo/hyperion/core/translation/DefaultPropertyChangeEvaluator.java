package com.dottydingo.hyperion.core.translation;

/**
 */
public class DefaultPropertyChangeEvaluator<T> implements PropertyChangeEvaluator<T>
{
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
