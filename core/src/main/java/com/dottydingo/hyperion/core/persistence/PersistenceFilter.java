package com.dottydingo.hyperion.core.persistence;


import com.dottydingo.hyperion.core.persistence.query.PersistentQueryBuilder;

/**
 * Defines filters that can optionally restrict access to data at the object level.
 */
public interface PersistenceFilter<P>
{
    /**
     * Return an optional filter that will be applied when processing a query request. This allows
     * data to be restricted based off of permissions or call context.
     * @param persistenceContext The context
     * @return A query builder to add criteria to the query, null if no filter should be applied
     */
    PersistentQueryBuilder getFilterQueryBuilder(PersistenceContext persistenceContext);

    /**
     * Used to determine if a specific persistent item can be retrieved by ID
     * @param persistentObject The object
     * @param persistenceContext The context
     * @return true if the item should be visible to the caller, false otherwise. If false
     * is returned then this item will be omitted from the response
     */
    boolean isVisible(P persistentObject,PersistenceContext persistenceContext);

    /**
     * Used to determine if the specified persistent object can be updated. THis allows update restrictions
     * to be added at the object level.
     * @param persistentObject The object to update
     * @param persistenceContext The context
     * @return True if the item can be created, false otherwise.
     */
    boolean canUpdate(P persistentObject, PersistenceContext persistenceContext);

    /**
     * Used to determin if the specified persistent object can be deleted. This allows delete restrictions
     * to be added at the object level.
     * @param persistentObject The object to delete
     * @param persistenceContext The context
     * @return True if the item can be deleted, false otherwise.
     */
    boolean canDelete(P persistentObject, PersistenceContext persistenceContext);
}
