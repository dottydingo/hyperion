package com.dottydingo.hyperion.core.translation;

import java.util.Collection;

/**
 * Property change evaluator for collection classes. This is required because the ORM may return a custom collection
 * implementation so equals will fail even if the contents of the collection are the same
 */
public class CollectionPropertyChangeEvaluator implements PropertyChangeEvaluator<Collection>
{
    @Override
    public boolean hasChanged(Collection oldValue, Collection newValue)
    {
        return !equals(oldValue,newValue);
    }

    private boolean equals(Collection object1, Collection object2)
    {
        if (object1 == object2)
        {
            return true;
        }
        if ((object1 == null) || (object2 == null))
        {
            return false;
        }
        return object1.size() == object2.size() && object1.containsAll(object2);
    }
}
