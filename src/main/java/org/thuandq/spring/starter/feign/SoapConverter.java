package org.thuandq.spring.starter.feign;

import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jaxb.JAXBContextFactory;
import feign.soap.SOAPDecoder;
import feign.soap.SOAPEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SoapConverter {
    private static final JAXBContextFactory jaxbFactory = new JAXBContextFactory.Builder()
            .withMarshallerJAXBEncoding("UTF-8")
            .build();

    @Bean
    public Encoder feignEncoder() {
        return new SOAPEncoder(jaxbFactory);
    }

    @Bean
    public Decoder feignDecoder() {
        return new SOAPDecoder(jaxbFactory);
    }

}
