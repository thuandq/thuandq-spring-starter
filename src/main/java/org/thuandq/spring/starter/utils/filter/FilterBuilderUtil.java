package org.thuandq.spring.starter.utils.filter;

import org.thuandq.spring.starter.exception.BusinessException;
import org.thuandq.spring.starter.utils.ResponseCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;


public final class FilterBuilderUtil {
    private static final Logger logger = LogManager.getLogger(FilterBuilderUtil.class);
    private static final String FILTER_SEARCH_DELIMITER = "&";
    private static final String FILTER_CONDITION_DELIMITER = "\\|";
    private static final String LIST_DELIMITER = ",";
    public static final String DOT = "\\.";

    private FilterBuilderUtil() {
    }

    public static <T> SpecificationBuilder<T> createFilterSpecification(String andFilter, String orFilter, Map<String, BiFunction<String, String, ?>> typeConverters) {
        List<SearchCriteria> andCriteria = createFilterCondition(andFilter, typeConverters);
        List<SearchCriteria> orCriteria = createFilterCondition(orFilter, typeConverters);
        SpecificationBuilder<T> builder = new SpecificationBuilder<>();
        andCriteria.forEach(builder::and);
        orCriteria.forEach(builder::or);
        return builder;
    }

    public static List<SearchCriteria> createFilterCondition(String criteria, Map<String, BiFunction<String, String, ?>> typeConverters) {
        if (Strings.isBlank(criteria)) {
            return Collections.emptyList();
        }
        List<String> expressions = splitStringToList(criteria, FILTER_SEARCH_DELIMITER);
        if (expressions.isEmpty()) {
            return Collections.emptyList();
        }
        return expressions.stream().map(expression -> splitStringToList(expression, FILTER_CONDITION_DELIMITER))
                .map(expression -> getCriteria(typeConverters, expression))
                .collect(Collectors.toList());
    }

    private static SearchCriteria getCriteria(Map<String, BiFunction<String, String, ?>> typeConverters, List<String> expressionSeeds) {
        try {
            logger.info("Start getting criteria in {}", expressionSeeds);
            var stringOperator = expressionSeeds.get(1);
            var operator = SearchOperator.fromValue(stringOperator);
            var stringValue = URLDecoder.decode(expressionSeeds.get(2), StandardCharsets.UTF_8);
            var field = expressionSeeds.get(0);
            if (operator != null) {
                if (isOperatorOr(operator)) {
                    List<String> keys = Arrays.stream(field.split(LIST_DELIMITER)).toList();
                    List<?> convertedValues = keys
                            .stream().map(key -> {
                                BiFunction<String, String, ?> typeConverter = getTypeConvertor(typeConverters, key);
                                return typeConverter.apply(key, stringValue);
                            }).toList();
                    return new SearchCriteria<>(keys, operator, convertedValues);
                } else {
                    BiFunction<String, String, ?> typeConverter = getTypeConvertor(typeConverters, field);
                    if (operator == SearchOperator.IN || operator == SearchOperator.NOT_IN) {
                        List<?> convertedValues = Arrays.stream(stringValue.split(LIST_DELIMITER))
                                .map(value -> typeConverter.apply(field, value))
                                .collect(Collectors.toList());
                        return new SearchCriteria<>(field, operator, convertedValues);
                    }
                    return new SearchCriteria<>(field, operator, typeConverter.apply(field, stringValue));
                }
            } else {
                throw new BusinessException(ResponseCode.INVALID_FILTER_OPERATOR,
                        String.format("Operator %s is invalid, accepted: %s", stringOperator,
                                Arrays.toString(SearchOperator.values())));
            }
        } catch (IndexOutOfBoundsException e) {
            throw new BusinessException(ResponseCode.MISSING_PARAMETER, ResponseCode.MISSING_PARAMETER.getMessage());
        }
    }

    private static boolean isOperatorOr(SearchOperator operator) {
        return SearchOperator.OR_LIKE.equals(operator) || SearchOperator.OR_EQUAL.equals(operator);
    }

    private static BiFunction<String, String, ?> getTypeConvertor(Map<String, BiFunction<String, String, ?>> typeConverters, String field) {
        BiFunction<String, String, ?> typeConverter = typeConverters.get(field);
        if (typeConverter == null) {
            throw new BusinessException(ResponseCode.INVALID_FIELD_NAME, ResponseCode.INVALID_FIELD_NAME.getMessage());
        }
        return typeConverter;
    }

    private static List<String> splitStringToList(String search, String delimiter) {
        return Arrays.asList(search.split(delimiter));
    }
}
