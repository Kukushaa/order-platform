package com.kukusha.token_service.service;

import com.kukusha.token_service.model.TokenType;
import com.kukusha.token_service.processor.TokenProcessor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TokenProcessorDriver {
    private final Map<TokenType, TokenProcessor> tokenProcessors;

    public TokenProcessorDriver(List<TokenProcessor> processors) {
        this.tokenProcessors = processors.stream()
                .collect(Collectors.toMap(TokenProcessor::type, Function.identity()));
    }

    public TokenProcessor getTokenProcessor(TokenType tokenType) {
        if (tokenType == null) {
            throw new NullPointerException("TokenType is null");
        }

        return tokenProcessors.get(tokenType);
    }
}
