package com.dottydingo.hyperion.core.translation;

import java.util.Date;

/**
 * Property change evaluator for Date classes. This is required because the ORM may return a Timestamp instead
 * of a date class so the equals() will fail even if the values are the same.
 */
public class DatePropertyChangeEvaluator implements PropertyChangeEvaluator<Date>
{
    @Override
    public boolean hasChanged(Date oldValue, Date newValue)
    {
        return !equals(oldValue,newValue);
    }

    private boolean equals(Date object1, Date object2)
    {
        if (object1 == object2)
        {
            return true;
        }
        if ((object1 == null) || (object2 == null))
        {
            return false;
        }
        return object1.getTime() == object2.getTime();
    }
}
