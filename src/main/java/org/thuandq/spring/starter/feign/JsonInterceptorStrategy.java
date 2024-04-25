package org.thuandq.spring.starter.feign;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;

import java.util.List;
import java.util.Objects;

public abstract class JsonInterceptorStrategy implements OkHttpInterceptorStrategy {
    List<String> mediaTypes = List.of("text", "application");

    @Override
    public boolean isSupportLogResponse(Response response) {
        if (Objects.isNull(response.body())) return false;
        return mediaTypeSupport(response.body().contentType());
    }

    @Override
    public boolean isSupportLogRequest(Request request) {
        if (Objects.isNull(request.body())) return false;
        return mediaTypeSupport(request.body().contentType());
    }

    private boolean mediaTypeSupport(MediaType mediaType) {
        return !Objects.isNull(mediaType)
                && mediaTypes.contains(mediaType.type());
    }
}
