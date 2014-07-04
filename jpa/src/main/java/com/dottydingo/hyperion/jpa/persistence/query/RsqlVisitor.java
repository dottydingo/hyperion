package com.dottydingo.hyperion.jpa.persistence.query;

import com.dottydingo.hyperion.api.exception.BadRequestException;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import cz.jirutka.rsql.parser.ast.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 */
public class RsqlVisitor extends NoArgRSQLVisitorAdapter<Predicate>
{
    public static final String INVALID_QUERY_FIELD = "ERROR_INVALID_QUERY_FIELD";
    private CriteriaBuilder cb;
    private Map<String,JpaEntityQueryBuilder> queryBuilders;
    private Root<?> entityRoot;
    private PersistenceContext context;


    public RsqlVisitor(PersistenceContext context, Root<?> entityRoot, CriteriaBuilder cb, Map<String,
            JpaEntityQueryBuilder> queryBuilders)
    {
        this.context = context;
        this.cb = cb;
        this.queryBuilders = queryBuilders;
        this.entityRoot = entityRoot;
    }

    @Override
    public Predicate visit(AndNode node)
    {
        return cb.and(getChildPredicates(node));
    }


    @Override
    public Predicate visit(OrNode node)
    {
        return cb.or(getChildPredicates(node));
    }

    @Override
    public Predicate visit(EqualNode node)
    {
        return getQueryBuilder(node).buildPredicate(entityRoot,cb,ComparisonOperator.EQUAL,node.getArguments(), context);
    }

    @Override
    public Predicate visit(InNode node)
    {
        return getQueryBuilder(node).buildPredicate(entityRoot,cb,ComparisonOperator.IN,node.getArguments(), context);
    }

    @Override
    public Predicate visit(GreaterThanOrEqualNode node)
    {
        return getQueryBuilder(node).buildPredicate(entityRoot,cb,ComparisonOperator.GREATER_EQUAL,node.getArguments(),
                context);
    }

    @Override
    public Predicate visit(GreaterThanNode node)
    {
        return getQueryBuilder(node).buildPredicate(entityRoot,cb,ComparisonOperator.GREATER_THAN,node.getArguments(),
                context);
    }

    @Override
    public Predicate visit(LessThanOrEqualNode node)
    {
        return getQueryBuilder(node).buildPredicate(entityRoot,cb,ComparisonOperator.LESS_EQUAL,node.getArguments(),
                context);
    }

    @Override
    public Predicate visit(LessThanNode node)
    {
        return getQueryBuilder(node).buildPredicate(entityRoot,cb,ComparisonOperator.LESS_THAN,node.getArguments(),
                context);
    }

    @Override
    public Predicate visit(NotEqualNode node)
    {
        return getQueryBuilder(node).buildPredicate(entityRoot,cb,ComparisonOperator.NOT_EQUAL,node.getArguments(),
                context);
    }

    @Override
    public Predicate visit(NotInNode node)
    {
        return getQueryBuilder(node).buildPredicate(entityRoot,cb,ComparisonOperator.NOT_IN,node.getArguments(),
                context);
    }

    protected Predicate[] getChildPredicates(LogicalNode node)
    {
        List<Predicate> predicates = new ArrayList<>();
        for (Node child : node)
        {
            predicates.add(child.accept(this));
        }
        return predicates.toArray(new Predicate[predicates.size()]);
    }

    protected JpaEntityQueryBuilder getQueryBuilder(ComparisonNode comparison)
    {
        JpaEntityQueryBuilder qb = queryBuilders.get(comparison.getSelector());
        if(qb == null)
            throw new BadRequestException(context.getMessageSource().getErrorMessage(INVALID_QUERY_FIELD,
                    context.getLocale(),comparison.getSelector()));

        return qb;
    }
}
