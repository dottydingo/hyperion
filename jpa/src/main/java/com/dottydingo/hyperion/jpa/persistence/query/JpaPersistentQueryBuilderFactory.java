package com.dottydingo.hyperion.jpa.persistence.query;

import com.dottydingo.hyperion.api.exception.BadRequestException;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import com.dottydingo.hyperion.core.persistence.query.PersistentQueryBuilderFactory;
import cz.jirutka.rsql.parser.model.ComparisonExpression;
import cz.jirutka.rsql.parser.model.Expression;
import cz.jirutka.rsql.parser.model.LogicalExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Map;

/**
 */
public class JpaPersistentQueryBuilderFactory implements PersistentQueryBuilderFactory<JpaPersistentQueryBuilder>
{
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public JpaPersistentQueryBuilder createQueryBuilder(Expression rootExpression, PersistenceContext persistenceContext)
    {
        return new InternalPredicateBuilder(persistenceContext.getApiVersionPlugin().getQueryBuilders(),rootExpression);
    }

    private class InternalPredicateBuilder<P> implements JpaPersistentQueryBuilder<P>
    {
        private Map<String,JpaEntityQueryBuilder> queryBuilders;
        private Expression rootExpression;

        private InternalPredicateBuilder(Map<String, JpaEntityQueryBuilder> queryBuilders, Expression rootExpression)
        {
            this.queryBuilders = queryBuilders;
            this.rootExpression = rootExpression;
        }

        @Override
        public Predicate buildPredicate(Root<P> root, CriteriaQuery<?> query, CriteriaBuilder cb)
        {
            return buildPredicate(rootExpression,root,cb);
        }



        private Predicate buildPredicate(Expression expression,  Root entityRoot, CriteriaBuilder cb)
        {
            logger.debug("Creating criterion for: {}", expression);

            if (expression.isLogical())
            {
                return buildPredicate((LogicalExpression) expression,entityRoot,cb);
            }
            if (expression.isComparison())
            {
                return buildPredicate((ComparisonExpression) expression,entityRoot,cb);
            }

            throw new BadRequestException("Invalid expression.");
        }

        private Predicate buildPredicate(LogicalExpression logical,  Root entityRoot, CriteriaBuilder cb)
        {
            switch (logical.getOperator())
            {
                case AND:
                    return cb.and(
                            buildPredicate(logical.getLeft(),entityRoot,cb),
                            buildPredicate(logical.getRight(),entityRoot,cb));

                case OR:
                    return cb.or(
                            buildPredicate(logical.getLeft(),entityRoot,cb),
                            buildPredicate(logical.getRight(),entityRoot,cb));
            }
            throw new BadRequestException("Invalid expression.");
        }

        private Predicate buildPredicate(ComparisonExpression comparison,  Root entityRoot, CriteriaBuilder cb)
        {
            JpaEntityQueryBuilder qb = queryBuilders.get(comparison.getSelector());
            if(qb == null)
                throw new BadRequestException(String.format("Can not query by %s",comparison.getSelector()));

            return qb.buildPredicate(entityRoot,cb,comparison.getOperator(),comparison.getArgument());
        }
    }
}
