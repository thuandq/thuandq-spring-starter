package org.thuandq.spring.starter.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
public final class Utility {
    public static String getClientMessageId() {
        return getHeader(Constants.CLIENT_MESSAGE_ID);
    }

    public static String getHeader(String header) {
        try {
            var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            var servletRequest = Objects.requireNonNull(attributes).getRequest();
            return servletRequest.getHeader(header);
        } catch (Exception e) {
            log.error("fail to get header");
        }
        return null;
    }

    public static <T> CompletableFuture<List<T>> toFutureList(List<CompletableFuture<T>> futures) {
        var allOf = CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        return allOf.thenApply(v -> futures.stream().map(CompletableFuture::join).filter(Objects::nonNull).collect(Collectors.toList()));
    }


    public static BigDecimal convertString2BigDecimal(String val) {
        return Objects.isNull(val) ? BigDecimal.ZERO : new BigDecimal(val);
    }

}
