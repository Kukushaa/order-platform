package com.kukusha.payment_service.config;

import com.kukusha.token_service.model.TokenType;
import com.kukusha.token_service.service.TokenProcessorDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@Configuration
public class JwtConfig {
    private final TokenProcessorDriver tokenProcessorDriver;

    public JwtConfig(TokenProcessorDriver tokenProcessorDriver) {
        this.tokenProcessorDriver = tokenProcessorDriver;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return tokenProcessorDriver.getTokenProcessor(TokenType.ACCESS).encObj().getJwtDecoder();
    }
}
