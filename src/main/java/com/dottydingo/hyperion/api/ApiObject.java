package com.dottydingo.hyperion.api;

import java.io.Serializable;

/**
 * An API object.
 */
public interface ApiObject<ID extends Serializable>
{
    /**
     * Return the Id of the API object.
     * @return the Id.
     */
    ID getId();

    /**
     * Set the id of the API object.
     * @param id The Id.
     */
    void setId(ID id);
}
