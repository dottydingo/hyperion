package com.dottydingo.hyperion.service.persistence.query;

import com.dottydingo.hyperion.service.configuration.EntityPlugin;

/**
 * User: mark
 * Date: 2/24/13
 * Time: 10:04 AM
 */
public interface PredicateBuilderFactory
{
    PredicateBuilder createPredicateBuilder(String query, EntityPlugin entityPlugin);
}
