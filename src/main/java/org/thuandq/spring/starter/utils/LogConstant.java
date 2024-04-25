package org.thuandq.spring.starter.utils;

public class LogConstant {
    public final static String FIELD_LOG_TYPE = "logType";
    public final static String FIELD_DURATION = "duration";

    public enum LogType {
        REQUEST("Request"),
        RESPONSE("Response"),
        FEIGN_REQUEST("FeignRequest"),
        FEIGN_RESPONSE("FeignResponse"),
        DB_QUERY("DBQuery"),
        DB_UPDATE("DBUpdate"),
        DB_INSERT("DBInsert");
        private final String value;

        LogType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
