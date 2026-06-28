package com.nimbusid.platform.config;

import com.nimbusid.platform.factory.ResponseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class PlatformConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public ResponseFactory responseFactory(Clock clock) {
        return new ResponseFactory(clock);
    }
}
