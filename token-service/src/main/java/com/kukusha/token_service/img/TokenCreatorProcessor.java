package com.kukusha.token_service.img;

import com.kukusha.token_service.model.TokenCreateDTO;
import com.kukusha.token_service.model.TokenResponse;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;

public interface TokenCreatorProcessor {
    JwtEncoder jwtEncoder();

    RSAKey rsaJwk();

    default TokenResponse createToken(TokenCreateDTO tokenCreateDTO) {
        Instant now = Instant.now();
        Instant exp = now.plus(tokenCreateDTO.expAt(), tokenCreateDTO.chronoUnit());

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer(tokenCreateDTO.issuer())
                .issuedAt(now)
                .expiresAt(exp)
                .subject(tokenCreateDTO.subject())
                .claims(claims -> claims.putAll(tokenCreateDTO.claims()))
                .build();

        JwsHeader jwsHeader = JwsHeader.with(SignatureAlgorithm.RS256)
                .keyId(rsaJwk().getKeyID())
                .build();

        String token = jwtEncoder().encode(JwtEncoderParameters.from(jwsHeader, claimsSet)).getTokenValue();

        return new TokenResponse(token, exp);
    }
}
