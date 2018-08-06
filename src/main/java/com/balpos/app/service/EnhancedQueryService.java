package com.balpos.app.service;

import io.github.jhipster.service.QueryService;
import io.github.jhipster.service.filter.Filter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.SingularAttribute;
import java.util.List;

public abstract class EnhancedQueryService<ENTITY> extends QueryService<ENTITY> {

    @Override
    protected <OTHER, X> Specification<ENTITY> buildReferringEntitySpecification(Filter<X> filter,
                                                                                 SingularAttribute<? super ENTITY, OTHER> reference,
                                                                                 SingularAttribute<OTHER, X> valueField) {
        if (filter.getEquals() != null) {
            return equalsSpecification(reference, valueField, filter.getEquals());
        } else if (filter.getIn() != null) {
            return valueIn(reference, valueField, filter.getIn());
        } else if (filter.getSpecified() != null) {
            return byFieldSpecified(reference, filter.getSpecified());
        }
        return null;
    }

    protected <OTHER, X> Specification<ENTITY> valueIn(SingularAttribute<? super ENTITY, OTHER> reference,
                                                       SingularAttribute<OTHER, X> idField,
                                                       List<X> values) {
        return (root, query, builder) -> {
            CriteriaBuilder.In<X> in = builder.in(root.get(reference).get(idField));
            for (X value : values) {
                in = in.value(value);
            }
            return in;
        };
    }
}
