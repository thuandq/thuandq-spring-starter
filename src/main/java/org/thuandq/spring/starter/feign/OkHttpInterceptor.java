package org.thuandq.spring.starter.feign;

import org.thuandq.spring.starter.utils.LogConstant;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.logging.log4j.ThreadContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Data
@Slf4j
@RequiredArgsConstructor
public class OkHttpInterceptor implements Interceptor {
    OkHttpInterceptorStrategy okHttpInterceptorStrategy;

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.currentTimeMillis();
        if (okHttpInterceptorStrategy.isSupportLogRequest(request)) {
            okHttpInterceptorStrategy.logRequest(request);
        }
        var response = chain.proceed(request);
        if (okHttpInterceptorStrategy.isSupportLogResponse(response)) {
            ThreadContext.put(LogConstant.FIELD_DURATION, "" + (System.currentTimeMillis() - startTime));
            response = okHttpInterceptorStrategy.logResponse(request, response);
            ThreadContext.remove(LogConstant.FIELD_DURATION);
        }
        return response;
    }
}
