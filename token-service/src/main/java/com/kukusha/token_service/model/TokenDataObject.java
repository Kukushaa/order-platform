package com.kukusha.token_service.model;

import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.security.oauth2.jwt.JwtEncoder;

public abstract class TokenDataObject {
    private final JwtEncoder jwtEncoder;
    private final RSAKey rsaJwk;

    public TokenDataObject(JwtEncoder jwtEncoder, RSAKey rsaJwk) {
        this.jwtEncoder = jwtEncoder;
        this.rsaJwk = rsaJwk;
    }

    public JwtEncoder getJwtEncoder() {
        return jwtEncoder;
    }

    public RSAKey getRsaJwk() {
        return rsaJwk;
    }
}
