package com.dottydingo.hyperion.core.persistence.query;

import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import cz.jirutka.rsql.parser.ast.Node;

/**
 */
public interface PersistentQueryBuilderFactory<B extends PersistentQueryBuilder>
{
    B createQueryBuilder(Node rootExpression, PersistenceContext persistenceContext);
}
