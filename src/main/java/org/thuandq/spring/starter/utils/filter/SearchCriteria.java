package org.thuandq.spring.starter.utils.filter;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class SearchCriteria<K, V> {
    private K key;
    private V value;
    private SearchOperator operator;

    public SearchCriteria(K key, SearchOperator operator, V value) {
        this.key = key;
        this.value = value;
        this.operator = operator;
    }


}
