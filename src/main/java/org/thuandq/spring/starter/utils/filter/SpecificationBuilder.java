package org.thuandq.spring.starter.utils.filter;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import javax.persistence.criteria.*;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.*;

import static org.thuandq.spring.starter.utils.filter.FilterBuilderUtil.DOT;

public class SpecificationBuilder<T> implements Specification<T>, Externalizable {
    public static final long serialVersionUID = 1L;
    private static final Map<SearchOperator, TriFunction<SearchCriteria<?, ?>, CriteriaBuilder, Root<?>, Predicate>> FILTER_CRITERIA = new EnumMap<>(SearchOperator.class);

    static {
        FILTER_CRITERIA.put(SearchOperator.GREATER_THAN, (criteria, criteriaBuilder, root) -> criteriaBuilder.greaterThan(getKey((String) criteria.getKey(), root), (Comparable) criteria.getValue()));
        FILTER_CRITERIA.put(SearchOperator.LESS_THAN, (criteria, criteriaBuilder, root) -> criteriaBuilder.lessThan(getKey((String) criteria.getKey(), root), (Comparable) criteria.getValue()));
        FILTER_CRITERIA.put(SearchOperator.EQUAL, (criteria, criteriaBuilder, root) -> criteriaBuilder.equal(getKey((String) criteria.getKey(), root), criteria.getValue()));
        FILTER_CRITERIA.put(SearchOperator.LIKE, (criteria, criteriaBuilder, root) -> criteriaBuilder.like(criteriaBuilder.upper(getKey((String) criteria.getKey(), root)), "%" + criteria.getValue().toString().toUpperCase() + "%"));
        FILTER_CRITERIA.put(SearchOperator.IN, (criteria, criteriaBuilder, root) -> criteriaBuilder.in(getKey((String) criteria.getKey(), root)).value(criteria.getValue()));
        FILTER_CRITERIA.put(SearchOperator.NOT_IN, (criteria, criteriaBuilder, root) -> criteriaBuilder.in(getKey((String) criteria.getKey(), root)).value(criteria.getValue()).not());
        FILTER_CRITERIA.put(SearchOperator.NOT_EQUAL, (criteria, criteriaBuilder, root) -> criteriaBuilder.notEqual(getKey((String) criteria.getKey(), root), criteria.getValue()));
        FILTER_CRITERIA.put(SearchOperator.GREATER_THAN_EQUAL, (criteria, criteriaBuilder, root) -> criteriaBuilder.greaterThanOrEqualTo(getKey((String) criteria.getKey(), root), (Comparable) criteria.getValue()));
        FILTER_CRITERIA.put(SearchOperator.LESS_THAN_EQUAL, (criteria, criteriaBuilder, root) -> criteriaBuilder.lessThanOrEqualTo(getKey((String) criteria.getKey(), root), (Comparable) criteria.getValue()));
        FILTER_CRITERIA.put(SearchOperator.OR_LIKE, (criteria, criteriaBuilder, root) ->
                {
                    var keys = (List<String>) criteria.getKey();
                    var values = (List<String>) criteria.getValue();

                    var predicates = keys.stream().map(key ->
                            FILTER_CRITERIA.get(SearchOperator.LIKE).apply(new SearchCriteria<>(key, SearchOperator.LIKE, values.get(keys.indexOf(key))), criteriaBuilder, root)
                    ).toArray(Predicate[]::new);
                    return criteriaBuilder.or(predicates);
                }
        );
        FILTER_CRITERIA.put(SearchOperator.OR_EQUAL, (criteria, criteriaBuilder, root) ->
                {

                    var keys = (List<String>) criteria.getKey();
                    var values = (List<String>) criteria.getValue();

                    var predicates = keys.stream().map(key ->
                            FILTER_CRITERIA.get(SearchOperator.EQUAL).apply(new SearchCriteria<>(key, SearchOperator.EQUAL, values.get(keys.indexOf(key))), criteriaBuilder, root)
                    ).toArray(Predicate[]::new);
                    return criteriaBuilder.or(predicates);
                }
        );
    }

    private static <T> Path<T> getKey(String key, Root<?> root) {
        if (key.contains(".")) {
            var splits = key.split(DOT);
            var table = root.join(splits[0]);
            return table.get(splits[1]);
        }
        return root.get(key);
    }

    private final transient List<SearchCriteria<?, ?>> andCriteriaList = new ArrayList<>();
    private final transient List<SearchCriteria<?, ?>> orCriteriaList = new ArrayList<>();

    public SpecificationBuilder() {
    }

    public void and(SearchCriteria<?, ?> criteria) {
        this.andCriteriaList.add(criteria);
    }

    public void or(SearchCriteria<?, ?> criteria) {
        this.orCriteriaList.add(criteria);
    }


    @Override
    public Predicate toPredicate(@NonNull Root<T> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicateList = new ArrayList<>();
        if (!this.andCriteriaList.isEmpty()) {
            var predicates = this.andCriteriaList.stream()
                    .map(searchCriteria -> FILTER_CRITERIA.get(searchCriteria.getOperator())
                            .apply(searchCriteria, criteriaBuilder, root))
                    .toArray(Predicate[]::new);
            predicateList.add(criteriaBuilder.and(predicates));
        }
        if (!this.orCriteriaList.isEmpty()) {
            var predicates = this.orCriteriaList.stream()
                    .map(searchCriteria -> FILTER_CRITERIA.get(searchCriteria.getOperator())
                            .apply(searchCriteria, criteriaBuilder, root))
                    .toArray(Predicate[]::new);
            predicateList.add(criteriaBuilder.or(predicates));
        }
        return criteriaBuilder.and(predicateList.toArray(Predicate[]::new));
    }

    private boolean isOperatorOr(SearchCriteria<?, ?> criteria) {
        return SearchOperator.OR_LIKE.equals(criteria.getOperator()) || SearchOperator.OR_EQUAL.equals(criteria.getOperator());
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        //by pass spot bug
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        //by pass spot bug
    }
}
