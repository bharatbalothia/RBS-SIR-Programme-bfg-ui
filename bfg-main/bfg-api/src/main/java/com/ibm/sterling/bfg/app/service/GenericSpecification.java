package com.ibm.sterling.bfg.app.service;

import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class GenericSpecification<T> {

    public static <T> Specification<T> filter(String text, String field) {
        final String finalText = text.toLowerCase();
        Specification<T> specification =
                (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    Boolean value = "true".equalsIgnoreCase(text) ? Boolean.TRUE :
                            "false".equalsIgnoreCase(finalText) ? Boolean.FALSE : null;

                    if (value != null) {
                        predicates.add(cb.equal(root.get(field), value));
                    } else {
                        if ("pending".equals(finalText)) {
                            predicates.add(cb.equal(root.get(field), 0));
                        } else {
                            predicates.add(cb.like(cb.lower(root.get(field)), "%" + finalText + "%"));
                        }
                    }
                    return cb.or(predicates.toArray(new Predicate[]{}));
                };
        return specification;
    }
}