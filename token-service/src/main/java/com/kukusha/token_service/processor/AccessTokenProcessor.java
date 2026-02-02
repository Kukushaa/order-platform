package com.kukusha.token_service.processor;

import com.kukusha.token_service.model.TokenObject;
import com.kukusha.token_service.model.TokenObjectImpl;
import com.kukusha.token_service.model.TokenType;
import com.kukusha.token_service.service.ResourceLoaderService;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenProcessor implements TokenProcessor {
    private final TokenObject tokenObject;

    public AccessTokenProcessor(ResourceLoaderService resourceLoaderService) {
        String privateKey = resourceLoaderService.loadResource("keys/access/private.pem");
        String publicKey = resourceLoaderService.loadResource("keys/access/public.pem");

        this.tokenObject = new TokenObjectImpl(privateKey, publicKey);
    }

    @Override
    public TokenObject encObj() {
        return tokenObject;
    }

    @Override
    public TokenType type() {
        return TokenType.ACCESS;
    }
}
