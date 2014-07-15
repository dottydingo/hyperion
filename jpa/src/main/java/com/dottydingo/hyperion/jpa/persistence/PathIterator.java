package com.dottydingo.hyperion.jpa.persistence;

import java.util.Iterator;

/**
 */
public class PathIterator implements Iterator<String>
{
    private String[] pathParts;
    int index = 0;

    public PathIterator(String path)
    {
        if(path == null || path.length() == 0)
            pathParts = new String[0];
        else
            pathParts = path.split("\\.");
    }

    @Override
    public boolean hasNext()
    {
        return index < pathParts.length;
    }

    @Override
    public String next()
    {
        if(index >= pathParts.length)
            throw new IndexOutOfBoundsException("Iterated past end.");

        return pathParts[index++];
    }

    @Override
    public void remove()
    {

    }

    public static PathIterator getPath(String path)
    {
        return new PathIterator(path);
    }
}
