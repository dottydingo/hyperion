package com.dottydingo.hyperion.jpa.persistence.query;

import com.dottydingo.hyperion.api.exception.BadRequestException;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import cz.jirutka.rsql.parser.ast.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class RsqlVisitor extends NoArgRSQLVisitorAdapter<Predicate>
{
    public static final String INVALID_QUERY_FIELD = "ERROR_INVALID_QUERY_FIELD";

    @SuppressWarnings("unchecked")
    private static final Map<cz.jirutka.rsql.parser.ast.ComparisonOperator, ComparisonOperator> OPERATORS_MAP = new HashMap() {{
        put( RSQLOperators.EQUAL,                 ComparisonOperator.EQUAL                 );
        put( RSQLOperators.IN,                    ComparisonOperator.IN                    );
        put( RSQLOperators.GREATER_THAN_OR_EQUAL, ComparisonOperator.GREATER_EQUAL );
        put( RSQLOperators.GREATER_THAN,          ComparisonOperator.GREATER_THAN          );
        put( RSQLOperators.LESS_THAN_OR_EQUAL,    ComparisonOperator.LESS_EQUAL    );
        put( RSQLOperators.LESS_THAN,             ComparisonOperator.LESS_THAN             );
        put( RSQLOperators.NOT_EQUAL,             ComparisonOperator.NOT_EQUAL             );
        put( RSQLOperators.NOT_IN,                ComparisonOperator.NOT_IN                );
    }};

    private CriteriaBuilder cb;
    private Map<String,JpaEntityQueryBuilder> queryBuilders;
    private Root<?> entityRoot;
    private CriteriaQuery<?> query;
    private PersistenceContext context;


    public RsqlVisitor(PersistenceContext context, Root<?> entityRoot, CriteriaQuery<?> query,CriteriaBuilder cb,
                       Map<String, JpaEntityQueryBuilder> queryBuilders)
    {
        this.context = context;
        this.cb = cb;
        this.queryBuilders = queryBuilders;
        this.query = query;
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
    public Predicate visit(ComparisonNode node)
    {
        return getQueryBuilder(node).buildPredicate(entityRoot,query,cb,
                OPERATORS_MAP.get(node.getOperator()),node.getArguments(),context);
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
