package com.kukusha.token_service.img;

import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenDriver implements TokenCreatorProcessor, RSAKeyGenerator, JwtEncoderProcessor {
    @Value("${jwt.access-token.private-key}")
    private String privateKey;

    @Value("${jwt.access-token.public-key}")
    private String publicKey;

    @Value("${jwt.access-token.algorythm}")
    private String algorythm;

    @Override
    public JwtEncoder jwtEncoder() {
        return jwtEncoder(rsaJwk());
    }

    @Override
    public RSAKey rsaJwk() {
        return getRSAKey(privateKey, publicKey, algorythm);
    }
}
