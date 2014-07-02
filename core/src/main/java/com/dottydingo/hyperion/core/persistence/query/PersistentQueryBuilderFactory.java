package com.dottydingo.hyperion.core.persistence.query;

import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.hyperion.core.registry.EntityPlugin;
import cz.jirutka.rsql.parser.model.Expression;

/**
 */
public interface PersistentQueryBuilderFactory<B extends PersistentQueryBuilder>
{
    B createQueryBuilder(Expression rootExpression, PersistenceContext persistenceContext);
}
