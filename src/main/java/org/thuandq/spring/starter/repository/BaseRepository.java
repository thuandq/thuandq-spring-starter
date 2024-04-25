package org.thuandq.spring.starter.repository;

import org.thuandq.spring.starter.utils.filter.FilterBuilderUtil;
import org.thuandq.spring.starter.utils.filter.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public interface BaseRepository<E, ID> extends JpaRepository<E, ID>, JpaSpecificationExecutor<E> {

    default List<E> findAll(String andFilter, String orFilter, Map<String, BiFunction<String, String, ?>> typeConverters,
                            SearchCriteria<?, ?>... criteria) {
        return findAll(andFilter, orFilter, typeConverters, List.of(criteria));
    }

    default List<E> findAll(String andFilter, String orFilter, Map<String, BiFunction<String, String, ?>> typeConverters,
                            Collection<SearchCriteria<?, ?>> criteria) {
        var specification = FilterBuilderUtil.<E>createFilterSpecification(andFilter, orFilter, typeConverters);
        criteria.forEach(specification::and);
        return findAll(specification);
    }

    default Page<E> findAll(String andFilter, String orFilter, Map<String, BiFunction<String, String, ?>> typeConverters,
                            Pageable pageable, SearchCriteria<?, ?>... criteria) {
        return findAll(andFilter, orFilter, typeConverters, pageable, List.of(criteria));
    }

    default Page<E> findAll(String andFilter, String orFilter, Map<String, BiFunction<String, String, ?>> typeConverters,
                            Pageable pageable, List<SearchCriteria<?, ?>> criteria) {
        var specification = FilterBuilderUtil.<E>createFilterSpecification(andFilter, orFilter, typeConverters);
        criteria.forEach(specification::and);
        return findAll(specification, pageable);
    }
}
