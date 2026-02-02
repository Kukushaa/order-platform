package com.kukusha.token_service.processor;

import com.kukusha.token_service.model.TokenCreateDTO;
import com.kukusha.token_service.model.TokenObject;
import com.kukusha.token_service.model.TokenResponse;
import com.kukusha.token_service.model.TokenType;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;

public interface TokenProcessor {
    TokenObject encObj();

    TokenType type();

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
                .keyId(encObj().getRsaJwk().getKeyID())
                .build();

        String token = encObj().getJwtEncoder().encode(JwtEncoderParameters.from(jwsHeader, claimsSet)).getTokenValue();

        return new TokenResponse(token, exp);
    }
}
