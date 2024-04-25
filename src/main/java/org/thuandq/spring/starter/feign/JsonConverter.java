package org.thuandq.spring.starter.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

@Component
public class JsonConverter {
    @Bean
    public Decoder springDecoder(ObjectMapper mapper) {
        SpringDecoder springDecoder = new SpringDecoder(convertersObjectFactory(mapper));
        return new ResponseEntityDecoder(springDecoder);
    }

    @Bean
    public Encoder springEncoder(ObjectMapper mapper) {
        return new SpringEncoder(convertersObjectFactory(mapper));
    }

    public ObjectFactory<HttpMessageConverters> convertersObjectFactory(ObjectMapper mapper) {
        var jacksonConverter = new MappingJackson2HttpMessageConverter(mapper);
        return () -> new HttpMessageConverters(jacksonConverter);
    }
}
