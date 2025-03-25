package com.yocy.common.web.config;

import com.yocy.common.web.decoder.FeignDecoder;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
@Configuration
public class FeignDecoderConfig {

    @Bean
    public Decoder feignDecoder(ObjectProvider<HttpMessageConverters> messageConverters) {
        return new OptionalDecoder((new ResponseEntityDecoder(new FeignDecoder(new SpringDecoder(messageConverters)))));
    }
}
