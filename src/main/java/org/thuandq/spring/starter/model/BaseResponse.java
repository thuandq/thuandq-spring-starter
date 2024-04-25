package org.thuandq.spring.starter.model;

import com.dslplatform.json.CompiledJson;
import org.thuandq.spring.starter.utils.Constants;
import org.thuandq.spring.starter.exception.BusinessErrorCode;
import org.thuandq.spring.starter.exception.BusinessException;
import org.thuandq.spring.starter.exception.FieldViolation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    private T data;
    private Metadata meta = new Metadata();

    @CompiledJson
    BaseResponse(T data, Metadata meta) {
        this.data = data;
        this.meta = meta;
    }

    public BaseResponse() {
    }

    public static <T> BaseResponse<T> ofSucceeded() {
        return ofSucceeded((T) null);
    }

    @SuppressWarnings("unchecked")
    public static <T> BaseResponse<T> ofSucceeded(T data) {
        BaseResponse<T> response = new BaseResponse<>();
        response.data = data;
        response.meta.code = Metadata.OK_CODE;
        return response;
    }

    public static <T> BaseResponse<List<T>> ofSucceeded(Page<T> data) {
        BaseResponse<List<T>> response = new BaseResponse<>();
        response.data = data.getContent();
        response.meta.code = Metadata.OK_CODE;
        response.meta.page = data.getNumber();
        response.meta.size = data.getSize();
        response.meta.total = data.getTotalElements();
        return response;
    }

    public static BaseResponse<Void> ofFailed(BusinessErrorCode errorCode) {
        return ofFailed(errorCode, (String) null);
    }

    public static BaseResponse<Void> ofFailed(BusinessErrorCode errorCode, List<FieldViolation> errors) {
        return ofFailed(errorCode, null, errors);
    }

    public static BaseResponse<Void> ofFailed(BusinessErrorCode errorCode, String message) {
        return ofFailed(errorCode, message, null);
    }

    public static BaseResponse<Void> ofFailed(BusinessErrorCode errorCode, String message, List<FieldViolation> errors) {
        BaseResponse<Void> response = new BaseResponse<>();
        response.meta.code = Constants.PREFIX_RESPONSE_CODE + "-" + errorCode.getCode();
        response.meta.message = message;
        response.meta.errors = errors;
        return response;
    }

    public static BaseResponse<Void> ofFailed(BusinessException exception) {
        return ofFailed(exception.getErrorCode(), exception.getMessage());
    }

    public T getData() {
        return data;
    }

    public Metadata getMeta() {
        return meta;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Metadata {
        public static final String OK_CODE = Constants.PREFIX_RESPONSE_CODE + "-" + 200;
        String code;
        Integer page;
        Integer size;
        Long total;
        String message;
        List<FieldViolation> errors;

        public Metadata() {
        }

        @CompiledJson
        public Metadata(String code, Integer page, Integer size, Long total, String message, List<FieldViolation> errors) {
            this.code = code;
            this.page = page;
            this.size = size;
            this.total = total;
            this.message = message;
            this.errors = errors;
        }

        public String getCode() {
            return code;
        }

        public Integer getPage() {
            return page;
        }

        public Integer getSize() {
            return size;
        }

        public Long getTotal() {
            return total;
        }

        public String getMessage() {
            return message;
        }

        public List<FieldViolation> getErrors() {
            return errors;
        }
    }
}
