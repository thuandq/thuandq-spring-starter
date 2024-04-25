package org.thuandq.spring.starter.feign;


import okhttp3.Request;
import okhttp3.Response;

public interface OkHttpInterceptorStrategy {
    boolean isSupportLogResponse(Response response);

    boolean isSupportLogRequest(Request request);

    void logRequest(Request request);

    Response logResponse(Request request, Response response);
}
