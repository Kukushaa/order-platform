package com.kukusha.token_service.model;

import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.security.oauth2.jwt.JwtEncoder;

public class TokenObjectDataImpl extends TokenDataObject {
    public TokenObjectDataImpl(String pk, String sc, JwtEncoder jwtEncoder) {
        super(null, null);
    }

    public TokenObjectDataImpl(JwtEncoder jwtEncoder, RSAKey rsaJwk) {
        super(jwtEncoder, rsaJwk);
    }
}
