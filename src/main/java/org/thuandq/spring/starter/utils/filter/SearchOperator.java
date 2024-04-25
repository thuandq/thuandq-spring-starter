package org.thuandq.spring.starter.utils.filter;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum SearchOperator {
    GREATER_THAN("gt"),
    LESS_THAN("lt"),
    GREATER_THAN_EQUAL("gte"),
    LESS_THAN_EQUAL("lte"),
    NOT_EQUAL("neq"),
    EQUAL("eq"),
    IN("in"),
    NOT_IN("nin"),
    LIKE("like"),
    OR_LIKE("ol"),
    OR_EQUAL("oeq");
    private static Map<String, SearchOperator> operators;
    private final String value;

    static {
        operators = new HashMap<>();
        for (SearchOperator operator : SearchOperator.values()) {
            operators.put(operator.value, operator);
        }
    }

    SearchOperator(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    public static SearchOperator fromValue(String value) {
        return operators.get(value.toLowerCase());
    }
}
