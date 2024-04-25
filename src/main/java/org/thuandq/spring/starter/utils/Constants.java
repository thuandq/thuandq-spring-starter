package org.thuandq.spring.starter.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {
    public static String PREFIX_RESPONSE_CODE;
    public static String GROUP_CODE_SUCCESS = "00";

    @Value("${response-code.prefix}")
    public void setSvnUrl(String prefixResponseCode) {
        PREFIX_RESPONSE_CODE = prefixResponseCode;
    }

    public static final String CLIENT_MESSAGE_ID = "clientMessageId";
}
