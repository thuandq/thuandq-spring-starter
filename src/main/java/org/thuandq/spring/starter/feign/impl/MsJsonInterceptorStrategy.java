package org.thuandq.spring.starter.feign.impl;

import org.thuandq.spring.starter.feign.JsonInterceptorStrategy;
import org.thuandq.spring.starter.feign.model.OkHttpMonitoringObject;
import org.thuandq.spring.starter.utils.LogConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import org.apache.commons.collections.MapUtils;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class MsJsonInterceptorStrategy extends JsonInterceptorStrategy {
    private final ObjectMapper mapper;
    private final List<String> ignoreHeaders = new ArrayList<>() {{
        add("authorization");
    }};

    @Override
    public void logRequest(Request request) {
        try {
            ThreadContext.put(LogConstant.FIELD_LOG_TYPE, LogConstant.LogType.FEIGN_REQUEST.getValue());
            String content = "";
            if (Objects.nonNull(request.body())) {
                Buffer buffer = new Buffer();
                request.body().writeTo(buffer);
                content = buffer.readUtf8();
            }
            var httpObject = new OkHttpMonitoringObject()
                    .setRequest(content)
                    .setHttpMethod(HttpMethod.resolve(request.method()))
                    .setHeaders(buildHeaders(request.headers().toMultimap()))
                    .setUrl(request.url().toString());
            log.info(mapper.writeValueAsString(httpObject));
            ThreadContext.remove(LogConstant.FIELD_LOG_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Response logResponse(Request request, Response response) {
        ResponseBody wrapperBody = null;
        try {
            ThreadContext.put(LogConstant.FIELD_LOG_TYPE, LogConstant.LogType.FEIGN_RESPONSE.getValue());
            String requestContent = "";
            if (Objects.nonNull(request.body())) {
                Buffer buffer = new Buffer();
                request.body().writeTo(buffer);
                requestContent = buffer.readUtf8();
            }
            var responseContent = Objects.nonNull(response.body()) ? response.body().bytes() : new byte[0];
            MediaType contentType = response.body().contentType();
            wrapperBody = ResponseBody.create(response.body().contentType(), responseContent);

            var httpObject = new OkHttpMonitoringObject()
                    .setRequest(requestContent)
                    .setResponse(new String(responseContent, charset(contentType)))
                    .setHttpMethod(HttpMethod.resolve(request.method()))
                    .setHeaders(buildHeaders(request.headers().toMultimap()))
                    .setUrl(request.url().toString())
                    .setHttpStatus(HttpStatus.resolve(response.code()));
            log.info(mapper.writeValueAsString(httpObject));
            ThreadContext.remove(LogConstant.FIELD_LOG_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.newBuilder().body(wrapperBody).build();
    }

    private Charset charset(MediaType type) {
        return Objects.nonNull(type) ? type.charset(StandardCharsets.UTF_8) : StandardCharsets.UTF_8;
    }

    private Map<String, List<String>> buildHeaders(Map<String, List<String>> requestHeaders) {
        if (MapUtils.isNotEmpty(requestHeaders)) {
            return requestHeaders.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                        if (!ignoreHeaders.contains(entry.getKey())) {
                            return entry.getValue();
                        }
                        return new ArrayList<>(List.of("<NOT TO LOGGING>"));
                    }));
        }

        return new HashMap<>();
    }
}
