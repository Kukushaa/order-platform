package com.kukusha.token_service.processor;

import com.kukusha.token_service.model.TokenObject;
import com.kukusha.token_service.model.TokenObjectImpl;
import com.kukusha.token_service.model.TokenType;
import com.kukusha.token_service.service.ResourceLoaderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenProcessor implements TokenProcessor {
    private final TokenObject tokenObject;

    public AccessTokenProcessor(ResourceLoaderService resourceLoaderService,
                                @Value("${jwt.access.keyId}")
                                String keyId) {
        String privateKey = resourceLoaderService.loadResource("keys/access/private.pem");
        String publicKey = resourceLoaderService.loadResource("keys/access/public.pem");
        this.tokenObject = new TokenObjectImpl(privateKey, publicKey, keyId);
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
