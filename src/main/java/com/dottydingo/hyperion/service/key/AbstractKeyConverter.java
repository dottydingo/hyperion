package com.dottydingo.hyperion.service.key;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 */
public abstract class AbstractKeyConverter<T extends Serializable> implements KeyConverter<T>
{
    @Override
    public List<T> covertKeys(String idValues)
    {
        List<T> list = new ArrayList<T>();
        if(idValues != null)
        {
            String[] split = idValues.split(",");
            for (String s : split)
            {
                list.add(convertValue(s.trim()));
            }
        }

        return list;
    }

    protected abstract T convertValue(String value);
}
