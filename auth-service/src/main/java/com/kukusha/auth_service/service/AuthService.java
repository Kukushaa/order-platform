package com.kukusha.auth_service.service;

import com.kukusha.auth_service.response.LoginResponse;
import com.kukusha.token_service.model.TokenCreateDTO;
import com.kukusha.token_service.model.TokenResponse;
import com.kukusha.token_service.model.TokenType;
import com.kukusha.token_service.service.TokenProcessorDriver;
import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {
    @Value("${token.issuer}")
    private String issuer;

    @Value("${token.access-minutes}")
    private int accessTokenMinutes;

    @Value("${token.refresh-days}")
    private int refreshTokenDays;

    private final AuthenticationManager authenticationManager;
    private final TokenProcessorDriver tokenProcessorDriver;

    public AuthService(AuthenticationManager authenticationManager, TokenProcessorDriver tokenProcessorDriver) {
        this.authenticationManager = authenticationManager;
        this.tokenProcessorDriver = tokenProcessorDriver;
    }

    public LoginResponse loginUser(String username, String password) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        List<String> userRoles = authenticate.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        TokenCreateDTO.TokenCreateDTOBuilder builder = new TokenCreateDTO.TokenCreateDTOBuilder()
                .issuer(issuer)
                .subject(authenticate.getName())
                .claims(Map.of("role", userRoles, "username", username));

        TokenResponse accessToken = tokenProcessorDriver.getTokenProcessor(TokenType.ACCESS)
                .createToken(builder
                        .expAt(accessTokenMinutes)
                        .chronoUnit(ChronoUnit.MINUTES)
                        .build());

        TokenResponse refreshToken = tokenProcessorDriver.getTokenProcessor(TokenType.REFRESH)
                .createToken(
                        builder
                                .expAt(refreshTokenDays)
                                .chronoUnit(ChronoUnit.DAYS)
                                .build());

        return new LoginResponse(accessToken.token(), refreshToken.token(), "Bearer");
    }

    public Map<String, Object> getPublicJWKAsJsonObject() {
        return new JWKSet(tokenProcessorDriver.getTokenProcessor(TokenType.ACCESS)
                .encObj()
                .getRsaJwk()
                .toPublicJWK())
                .toJSONObject();
    }
}
