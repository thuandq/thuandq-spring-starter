package org.thuandq.spring.starter.utils.filter;


import org.thuandq.spring.starter.exception.BusinessException;
import org.thuandq.spring.starter.utils.ResponseCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

public final class FieldTypeConverter {
  public static final BiFunction<String, String, Integer> CONVERT_INTEGER_FUNCTION = (fieldName, fieldValue) -> {
    try {
      return Integer.valueOf(fieldValue);
    } catch (NumberFormatException e) {
      throw new BusinessException(ResponseCode.NUMBER_FORMAT_ERROR, String.format("Invalid value %s for field %s, accepted type is Integer", fieldValue, fieldName));
    }
  };
  public static final BiFunction<String, String, Long> CONVERT_LONG_FUNCTION = (fieldName, fieldValue) -> {
    try {
      return Long.valueOf(fieldValue);
    } catch (NumberFormatException e) {
      throw new BusinessException(ResponseCode.NUMBER_FORMAT_ERROR, String.format("Invalid value %s for field %s, accepted type is Long", fieldValue, fieldName));
    }
  };
  public static final BiFunction<String, String, Float> CONVERT_FLOAT_FUNCTION = (fieldName, fieldValue) -> {
    try {
      return Float.valueOf(fieldValue);
    } catch (NumberFormatException e) {
      throw new BusinessException(ResponseCode.NUMBER_FORMAT_ERROR, String.format("Invalid value %s for field %s, accepted type is float", fieldValue, fieldName));
    }
  };
  public static final BiFunction<String, String, Boolean> CONVERT_BOOLEAN_FUNCTION = (fieldName, fieldValue) -> {
    Pattern booleanPattern = Pattern.compile("(^true$)|(^false$)");
    if (!booleanPattern.matcher(fieldValue).matches()) {
      throw new BusinessException(ResponseCode.BOOLEAN_FORMAT_ERROR, String.format("Invalid value %s for field %s, accepted type is boolean", fieldValue, fieldName));
    }
    return Boolean.parseBoolean(fieldValue);
  };
  public static final BiFunction<String, String, String> CONVERT_STRING_FUNCTION = (fieldName, fieldValue) -> fieldValue;
  public static final BiFunction<String, String, Date> CONVERT_DATE_FUNCTION = (fieldName, fieldValue) -> {
    try {
      SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      return formatter.parse(fieldValue);
    } catch (ParseException e) {
      throw new BusinessException(ResponseCode.FORMAT_DATE_INVALID, String.format("Invalid value %s for field %s, accepted type is date", fieldValue, fieldName));
    }
  };

  public static<T extends Enum<T>> BiFunction<String, String, T> CONVERT_ENUM_FUNCTION(Map<String, T> enumFieldValue) {
    return (fieldName, fieldValue) -> {
      T enumValue = enumFieldValue.get(fieldValue);
      if (enumValue == null) {
        throw new BusinessException(ResponseCode.ENUM_FIELD_VALUE_INVALID, String.format("Invalid value %s for field %s, accepted type is enum", fieldValue, fieldName));
      }
      return enumValue;
    };
  }

  private FieldTypeConverter() {
  }
}
