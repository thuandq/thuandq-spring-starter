package org.thuandq.spring.starter.feign;

import org.thuandq.spring.starter.feign.impl.MsJsonInterceptorStrategy;
import feign.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class FeignConfiguration {
    private final OkHttpInterceptor interceptor;
    private final MsJsonInterceptorStrategy interceptorStrategy;

    @Bean
    public okhttp3.OkHttpClient httpClient() {
        interceptor.setOkHttpInterceptorStrategy(interceptorStrategy);
        return new okhttp3.OkHttpClient().newBuilder()
                .addInterceptor(interceptor)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(Client.class)
    public Client feignClient(okhttp3.OkHttpClient client) {
        return new OkHttpClient(client);
    }
}
