package com.dottydingo.hyperion.service.persistence.query;

import com.dottydingo.hyperion.exception.BadRequestException;
import com.dottydingo.hyperion.service.configuration.EntityPlugin;
import cz.jirutka.rsql.parser.ParseException;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.TokenMgrError;
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
 * User: mark
 * Date: 2/24/13
 * Time: 7:56 AM
 */
public class RsqlPredicateBuilderFactory implements PredicateBuilderFactory
{
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public PredicateBuilder createPredicateBuilder(String query, EntityPlugin entityPlugin)
    {
        Expression queryTree;
        try
        {
            logger.debug("Parsing query: {}", query);
            queryTree = RSQLParser.parse(query);

        }
        catch (ParseException ex)
        {
            throw new BadRequestException("Error parsing query.", ex);
        }
        catch (TokenMgrError er)
        {
            throw new BadRequestException("Invalid query", er);
        }
        return new InternalPredicateBuilder(entityPlugin.getQueryBuilders(),queryTree);


    }

    private class InternalPredicateBuilder<P> implements PredicateBuilder<P>
    {
        private Map<String,QueryBuilder> queryBuilders;
        private Expression rootExpression;

        private InternalPredicateBuilder(Map<String, QueryBuilder> queryBuilders, Expression rootExpression)
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
            QueryBuilder qb = queryBuilders.get(comparison.getSelector());
            if(qb == null)
                throw new BadRequestException(String.format("Can not query by %s",comparison.getSelector()));

            return qb.buildPredicate(entityRoot,cb,comparison.getOperator(),comparison.getArgument());
        }
    }
}
