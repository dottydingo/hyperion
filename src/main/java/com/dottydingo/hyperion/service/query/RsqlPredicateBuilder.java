package com.dottydingo.hyperion.service.query;

import com.dottydingo.hyperion.service.exception.QueryException;
import cz.jirutka.rsql.parser.ParseException;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.TokenMgrError;
import cz.jirutka.rsql.parser.model.Comparison;
import cz.jirutka.rsql.parser.model.ComparisonExpression;
import cz.jirutka.rsql.parser.model.Expression;
import cz.jirutka.rsql.parser.model.LogicalExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import java.util.List;

/**
 * User: mark
 * Date: 9/16/12
 * Time: 11:24 AM
 */
public class RsqlPredicateBuilder implements PredicateBuilder
{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<AbstractPredicateBuilder> builders;
    private ArgumentParser argumentParser;
    private EntityManagerFactory entityManagerFactory;
    private Mapper mapper;

    public void setBuilders(List<AbstractPredicateBuilder> builders)
    {
        this.builders = builders;
    }

    public void setArgumentParser(ArgumentParser argumentParser)
    {
        this.argumentParser = argumentParser;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory)
    {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void setMapper(Mapper mapper)
    {
        this.mapper = mapper;
    }

    @Override
    public Predicate buildPredicate(String query, Class entityClass, Root root, CriteriaBuilder cb)
    {
        Expression queryTree;
        try
        {
            logger.info("Parsing query: {}", query);
            queryTree = RSQLParser.parse(query);

        }
        catch (ParseException ex)
        {
            throw new QueryException("Error parsing query.", ex);
        }
        catch (TokenMgrError er)
        {
            throw new QueryException("Invalid query", er);
        }
        return new InternalExpressionBuilder(cb).buildPredicate(queryTree,root);
    }

    private class InternalExpressionBuilder implements ExpressionBuilder
    {

        private CriteriaBuilder cb;


        private InternalExpressionBuilder(CriteriaBuilder cb)
        {
            this.cb = cb;
        }

        private Predicate buildPredicate(Expression expression,  Root entityRoot)
        {
            logger.trace("Creating criterion for: {}", expression);

            if (expression.isLogical())
            {
                return buildPredicate((LogicalExpression) expression,entityRoot);
            }
            if (expression.isComparison())
            {
                return buildPredicate((ComparisonExpression) expression,entityRoot);
            }

            throw new RuntimeException("Invalid expression.");
        }

        private Predicate buildPredicate(LogicalExpression logical,  Root entityRoot)
        {
            switch (logical.getOperator())
            {
                case AND:
                    return cb.and(
                            buildPredicate(logical.getLeft(),entityRoot),
                            buildPredicate(logical.getRight(),entityRoot));

                case OR:
                    return cb.or(
                            buildPredicate(logical.getLeft(),entityRoot),
                            buildPredicate(logical.getRight(),entityRoot));
            }
            throw new RuntimeException("Invalid expression.");
        }

        private Predicate buildPredicate(ComparisonExpression comparison,  Root entityRoot)
        {
            String property = mapper.translate(comparison.getSelector(), entityRoot.getModel().getJavaType());

            try
            {
                return delegateToBuilder(entityRoot, property, comparison.getOperator(), comparison.getArgument());

            }
            /*catch (ArgumentFormatException ex)
            {
                throw new RSQLException(
                        new ArgumentFormatException(comparison.getSelector(), ex.getArgument(), ex.getPropertyType()));
            }
            catch (UnknownSelectorException ex)
            {
                throw new RSQLException(ex);
            }*/
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Predicate delegateToBuilder(From entityRoot, String property, Comparison operator, String argument)
                throws ArgumentFormatException, UnknownSelectorException, IllegalArgumentException
        {
            Class<?> entityClass = entityRoot.getJavaType();
            for (AbstractPredicateBuilder builder : builders) {
                if (builder.accept(property, entityClass, this)) {
                    logger.debug("Delegating comparison [{} {} {}] on entity {} to builder: {}",
                            new Object[]{property, operator, argument, entityClass.getSimpleName(), builder.getClass().getSimpleName()});

                    return builder.createPredicate(property, operator, argument, entityRoot, "", this);
                }
            }

            throw new IllegalArgumentException("No Criterion Builder found for property " + property + " of " + entityClass);
        }

        @Override
        public ArgumentParser getArgumentParser() {
            return argumentParser;
        }

        @Override
        public EntityType getEntityType(Class<?> entityClass) {
            return entityManagerFactory.getMetamodel().entity(entityClass);
        }

        @Override
        public CriteriaBuilder getCriteriaBuilder()
        {
            return cb;
        }

        @Override
        public Mapper getMapper() {
            return mapper;
        }

    }


}

