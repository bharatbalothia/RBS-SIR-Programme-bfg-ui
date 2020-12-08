package com.ibm.sterling.bfg.app.service;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public class GenericSpecification {

    public static <T> Specification<T> filter(String text, String field) {
        final String finalText = Optional.ofNullable(text).map(String::toLowerCase).orElse("");
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Boolean value = "true".equalsIgnoreCase(text) ? Boolean.TRUE :
                    "false".equalsIgnoreCase(finalText) ? Boolean.FALSE : null;

            if (value != null) {
                predicates.add(cb.equal(root.get(field), value));
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
                    if (finalText.isEmpty()) {
                        predicates.add(from.get("requestorDN").isNull());
                        predicates.add(from.get("responderDN").isNull());
                    } else {
                        predicates.add(valueContains.apply(from, "requestorDN"));
                        predicates.add(valueContains.apply(from, "responderDN"));
                    }
                } else {
                    predicates.add(valueContains.apply(root, field));
                }
            }
            return cb.or(predicates.toArray(new Predicate[]{}));
        };
    }

}
