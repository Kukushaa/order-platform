package com.kukusha.token_service.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.kukusha.token_service")
@PropertySource("classpath:token-service.properties")
public class TokenServiceAutoConfiguration {
}
