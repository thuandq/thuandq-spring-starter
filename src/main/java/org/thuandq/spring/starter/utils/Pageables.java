package org.thuandq.spring.starter.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Pageables {
    private static final Pattern orderPattern = Pattern.compile("\\G(\\w+):(ASC|DESC)(,+|$)");

    public static Pageable of(int page, int size, String sorts) {
        var sort = sorts == null ? Sort.unsorted() : parseSort(sorts);
        return PageRequest.of(page, size, sort);
    }

    private static Sort parseSort(String sorts) {
        var orders = new ArrayList<Sort.Order>();
        var matcher = orderPattern.matcher(sorts);
        String fieldName;
        Sort.Direction direction;
        while (matcher.find()) {
            fieldName = matcher.group(1);
            direction = Sort.Direction.valueOf(matcher.group(2));
            orders.add(new Sort.Order(direction, fieldName));
        }
        return orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);
    }
}