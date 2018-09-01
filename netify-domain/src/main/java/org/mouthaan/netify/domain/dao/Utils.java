package org.mouthaan.netify.domain.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Utils {
    @SuppressWarnings("unchecked")
    static Predicate getPredicate(List<SearchCriteria> params, CriteriaBuilder builder, Root r, javax.persistence.criteria.Predicate predicate) {
        for (final SearchCriteria param : params) {
            boolean paramValueIsDate = false;
            Date parsedValue = null;

            if (param.getValue().toString().matches("\\d{4}-\\d{2}-\\d{2}")) {
                paramValueIsDate = true;
                parsedValue = getDateFromString(param.getValue().toString());
            }

            if (param.getOperation().equalsIgnoreCase(">")) {
                if (paramValueIsDate) {
                    predicate = builder.and(predicate, builder.greaterThanOrEqualTo(r.get(param.getKey()), parsedValue));
                } else {
                    predicate = builder.and(predicate, builder.greaterThanOrEqualTo(r.get(param.getKey()), param.getValue().toString()));
                }
            } else if (param.getOperation().equalsIgnoreCase("<")) {
                if (paramValueIsDate) {
                    predicate = builder.and(predicate, builder.lessThanOrEqualTo(r.get(param.getKey()), parsedValue));
                } else {
                    predicate = builder.and(predicate, builder.lessThanOrEqualTo(r.get(param.getKey()), param.getValue().toString()));
                }
            } else if (param.getOperation().equalsIgnoreCase("%")) {
                if (r.get(param.getKey()).getJavaType() == String.class) {
                    predicate = builder.and(predicate, builder.like(r.get(param.getKey()), "%" + param.getValue() + "%"));
                }
            } else if (param.getOperation().equalsIgnoreCase("=")) {
                predicate = builder.and(predicate, builder.equal(r.get(param.getKey()), param.getValue()));

            }
        }
        return predicate;
    }

    private static Date getDateFromString(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = null;
        try {
            parsedDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parsedDate;
    }
}
