package com.kukusha.token_service.processor;

import com.kukusha.token_service.model.TokenObject;
import com.kukusha.token_service.model.TokenObjectImpl;
import com.kukusha.token_service.model.TokenType;
import com.kukusha.token_service.service.ResourceLoaderService;
import org.springframework.beans.factory.annotation.Value;

public class StripeTokenProcessor implements TokenProcessor {
    private final TokenObject tokenObject;

    public StripeTokenProcessor(ResourceLoaderService resourceLoaderService,
                                @Value("${jwt.payment.keyId}")
                                String keyId) {
        String privateKey = resourceLoaderService.loadResource("keys/payment/private.pem");
        String publicKey = resourceLoaderService.loadResource("keys/payment/public.pem");
        this.tokenObject = new TokenObjectImpl(privateKey, publicKey, keyId);
    }

    @Override
    public TokenObject encObj() {
        return tokenObject;
    }

    @Override
    public TokenType type() {
        return TokenType.PAYMENT;
    }
}
