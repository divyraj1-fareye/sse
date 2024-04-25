package com.techprimers.springboot.db;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SSEConfiguration {

    @Bean
    public SSEEventEmitter sseEventEmitter() {
        return new SSEEventEmitter();
    }

}
