package com.kukusha.token_service.processor;

import com.kukusha.token_service.model.TokenObject;
import com.kukusha.token_service.model.TokenObjectImpl;
import com.kukusha.token_service.model.TokenType;
import com.kukusha.token_service.service.ResourceLoaderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenProcessor implements TokenProcessor {
    private final TokenObject tokenObject;

    public RefreshTokenProcessor(ResourceLoaderService resourceLoaderService,
                                 @Value("${jwt.refresh.keyId}")
                                 String refreshKeyId) {
        String publicKey = resourceLoaderService.loadResource("keys/refresh/public.pem");
        String privateKey = resourceLoaderService.loadResource("keys/refresh/private.pem");

        this.tokenObject = new TokenObjectImpl(privateKey, publicKey, refreshKeyId);
    }

    @Override
    public TokenObject encObj() {
        return tokenObject;
    }

    @Override
    public TokenType type() {
        return TokenType.REFRESH;
    }
}
