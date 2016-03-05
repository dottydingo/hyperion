package com.dottydingo.hyperion.api;

/**
 * The response from a delete operation
 */
public class DeleteResponse
{
    private int count;

    /**
     * Return the number of items deleted
     * @return The number of items
     */
    public int getCount()
    {
        return count;
    }

    /**
     * Set the number of items deleted
     * @param count The number of items
     */
    public void setCount(int count)
    {
        this.count = count;
    }
}
