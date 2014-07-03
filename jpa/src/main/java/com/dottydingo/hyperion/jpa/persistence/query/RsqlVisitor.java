package com.dottydingo.hyperion.jpa.persistence.query;

import com.dottydingo.hyperion.api.exception.BadRequestException;
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
    private CriteriaBuilder cb;
    private Map<String,JpaEntityQueryBuilder> queryBuilders;
    private Root<?> entityRoot;


    public RsqlVisitor(Root<?> entityRoot, CriteriaBuilder cb, Map<String, JpaEntityQueryBuilder> queryBuilders)
    {
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
        return getQueryBuilder(node).buildPredicate(entityRoot,cb,ComparisonOperator.EQUAL,node.getArguments());
    }

    @Override
    public Predicate visit(InNode node)
    {
        return getQueryBuilder(node).buildPredicate(entityRoot,cb,ComparisonOperator.IN,node.getArguments());
    }

    @Override
    public Predicate visit(GreaterThanOrEqualNode node)
    {
        return getQueryBuilder(node).buildPredicate(entityRoot,cb,ComparisonOperator.GREATER_EQUAL,node.getArguments());
    }

    @Override
    public Predicate visit(GreaterThanNode node)
    {
        return getQueryBuilder(node).buildPredicate(entityRoot,cb,ComparisonOperator.GREATER_THAN,node.getArguments());
    }

    @Override
    public Predicate visit(LessThanOrEqualNode node)
    {
        return getQueryBuilder(node).buildPredicate(entityRoot,cb,ComparisonOperator.LESS_EQUAL,node.getArguments());
    }

    @Override
    public Predicate visit(LessThanNode node)
    {
        return getQueryBuilder(node).buildPredicate(entityRoot,cb,ComparisonOperator.LESS_THAN,node.getArguments());
    }

    @Override
    public Predicate visit(NotEqualNode node)
    {
        return getQueryBuilder(node).buildPredicate(entityRoot,cb,ComparisonOperator.NOT_EQUAL,node.getArguments());
    }

    @Override
    public Predicate visit(NotInNode node)
    {
        return getQueryBuilder(node).buildPredicate(entityRoot,cb,ComparisonOperator.NOT_IN,node.getArguments());
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
            throw new BadRequestException(String.format("Can not query by %s", comparison.getSelector()));

        return qb;
    }
}
