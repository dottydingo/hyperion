package com.dottydingo.hyperion.service.model;

import java.io.Serializable;

/**
 * Interface for persistent objects
 * @param <ID> The type of the id
 */
public interface PersistentObject<ID extends Serializable>
{
    /**
     * Return the value of the Id
     * @return The ID
     */
    ID getId();

    /**
     * Set the value of the Id
     * @param id The Id
     */
    void setId(ID id);

}
