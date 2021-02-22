package com.ibm.sterling.bfg.app.service;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public class GenericSpecification {

    public static <T> Specification<T> filter(String field, String value) {
        final String finalText = Optional.ofNullable(value).map(String::toLowerCase).orElse("");
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Boolean boolValue = "true".equalsIgnoreCase(value) ? Boolean.TRUE :
                    "false".equalsIgnoreCase(finalText) ? Boolean.FALSE : null;

            if (boolValue != null) {
                predicates.add(cb.equal(root.get(field), boolValue));
            } else {
                BiFunction<From, String, Predicate> valueContains = (object, fieldValue) ->
                        cb.like(cb.lower(object.get(fieldValue)), "%" + finalText + "%");
                if ("pending".equals(finalText)) {
                    predicates.add(cb.equal(root.get(field), 0));
                } else if ("swiftDN".equals(field)) {
                    From from;
                    if ("ChangeControl".equals(root.getModel().getName())) {
                        from = root.join("entityLog");
                    } else from = root;
                    if (finalText.isEmpty())
                        predicates.add(cb.and(from.get("requestorDN").isNull(), from.get("responderDN").isNull()));
                    predicates.add(valueContains.apply(from, "requestorDN"));
                    predicates.add(valueContains.apply(from, "responderDN"));
                } else {
                    predicates.add(valueContains.apply(root, field));
                }
            }
            return cb.or(predicates.toArray(new Predicate[]{}));
        };
    }

    public static <T> Specification<T> equals(String field, String value) {
        return (root, query, cb) -> cb.equal(root.get(field), value);
    }

}
