package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class WebConfig {

    @Bean
    public JsonMapper jsonMapper() {
        return new JsonMapper();
    }
}
