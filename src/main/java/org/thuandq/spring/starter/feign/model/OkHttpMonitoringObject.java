package org.thuandq.spring.starter.feign.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class OkHttpMonitoringObject {
    private String request;
    private String response;
    private String url;
    private HttpStatus httpStatus;
    Map<String, List<String>> headers;
    HttpMethod httpMethod;
}
