package org.thuandq.spring.starter.utils;

import org.thuandq.spring.starter.exception.BusinessErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ResponseCode {
    public static final BusinessErrorCode INTERNAL_SERVER_ERROR =
            new BusinessErrorCode(5000, "Internal server error", 503);

    public static final BusinessErrorCode INTERNAL_STORAGE_ERROR =
            new BusinessErrorCode(5001, "Internal storage error", 503);
    public static final BusinessErrorCode INVALID_FIELD_FORMAT =
            new BusinessErrorCode(4013, "Invalid field format", 400);
    public static final BusinessErrorCode UNAUTHORIZED =
            new BusinessErrorCode(4016, "You need to login to to access this resource", 401);
    public static final BusinessErrorCode FORBIDDEN =
            new BusinessErrorCode(4017, "You don't have permission to to access this resource", 403);
    public static final BusinessErrorCode MISSING_PARAMETER =
            new BusinessErrorCode(4023, "Missing parameter", 400);
    public static final BusinessErrorCode REQUEST_TIMEOUT =
            new BusinessErrorCode(4024, "Request timeout", 500);
    public static final BusinessErrorCode ERROR_DATA_INPUT =
            new BusinessErrorCode(4025, "Request timeout", 500);
    public static final BusinessErrorCode FORMAT_DATE_INVALID =
            new BusinessErrorCode(4026, "Date format error", 400);
    public static final BusinessErrorCode INVALID_FIELD_NAME =
            new BusinessErrorCode(4027, "Field name is invalid", 400);
    public static final BusinessErrorCode BOOLEAN_FIELD_NAME =
            new BusinessErrorCode(4028, "Boolean name is invalid", 400);
    public static final BusinessErrorCode NUMBER_FORMAT_ERROR =
            new BusinessErrorCode(4029, "Number format error", 400);
    public static final BusinessErrorCode UNSUPPORTED_ACTION_REF =
            new BusinessErrorCode(4030, "Unsupported action ref", 400);
    public static final BusinessErrorCode ENUM_FIELD_VALUE_INVALID =
            new BusinessErrorCode(4031, "Enum value is invalid", 400);
    public static final BusinessErrorCode BOOLEAN_FORMAT_ERROR =
            new BusinessErrorCode(4033, "Invalid value of boolean type", 400);
    public static final BusinessErrorCode INVALID_FILTER_OPERATOR =
            new BusinessErrorCode(4034, "Invalid filter operator", 400);
}
