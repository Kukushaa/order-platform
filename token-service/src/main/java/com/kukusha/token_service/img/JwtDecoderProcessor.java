package com.kukusha.token_service.img;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

public interface JwtDecoderProcessor {
    default JwtDecoder jwtDecoder(RSAKey rsaJwk) throws JOSEException {
        return NimbusJwtDecoder.withPublicKey(rsaJwk.toRSAPublicKey()).build();
    }
}
