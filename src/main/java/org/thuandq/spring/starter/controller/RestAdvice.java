package org.thuandq.spring.starter.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.thuandq.spring.starter.utils.LogConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RestAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;
    private final List<String> ignorePath = List.of("swagger", "webjars", "actuator", "/v2/api-docs", "/error");


    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        if (body != null && ignorePath.stream().noneMatch(path -> request.getURI().toString().contains(path))) {
            try {
                ThreadContext.put(LogConstant.FIELD_LOG_TYPE, LogConstant.LogType.RESPONSE.getValue());
                log.info("Response {} {} : {}", request.getMethod(), request.getURI(), objectMapper.writeValueAsString(body));
                ThreadContext.remove(LogConstant.FIELD_LOG_TYPE);
            } catch (JsonProcessingException ignored) {
            }
        }

        return body;
    }

}